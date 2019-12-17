package com.demo.search;

import com.alibaba.druid.util.StringUtils;
import com.demo.blog.BlogService;
import com.demo.common.model.Blog;
import com.demo.common.model.TbWeekReport;
import com.demo.weekreport.WeekReportService;
import com.jfinal.aop.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *https://www.cnblogs.com/huangfox/archive/2012/08/10/2631240.html
 */
@Slf4j
public class SearchBlogServiceImpl implements SearchService {

    //每页查询20条数据
    public final static Integer PAGE_SIZE = 20;
    public final static String IndexDir = "d:\\luceneIndexDirBlog";

    @Inject
    BlogService service;

    @Override
    public ResultModel query(String queryString, String price, Integer page) throws Exception {
        long startTime = System.currentTimeMillis();

        //1. 需要使用的对象封装
        ResultModel resultModel = new ResultModel();
        //从第几条开始查询
        int start = (page - 1) * PAGE_SIZE;
        //查询到多少条为止
        Integer end = page * PAGE_SIZE;
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //创建组合查询对象
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        //2. 根据查询关键字封装查询对象
        QueryParser queryParser = new QueryParser("summary", analyzer);
        Query query1 = null;
        //判断传入的查询关键字是否为空, 如果为空查询所有, 如果不为空, 则根据关键字查询
        if (StringUtils.isEmpty(queryString)) {
            query1 = queryParser.parse("*:*");
        } else {
            query1 = queryParser.parse(queryString);
        }
        //将关键字查询对象, 封装到组合查询对象中
        builder.add(query1, BooleanClause.Occur.MUST);

        //3. 根据价格范围封装查询对象
        if (!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            Query query2 = IntPoint.newRangeQuery("price", Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            //将价格查询对象, 封装到组合查询对象中
            builder.add(query2, BooleanClause.Occur.MUST);
        }

        //4. 创建Directory目录对象, 指定索引库的位置
        /**
         * 使用MMapDirectory消耗的查询时间
         * ====消耗时间为=========324ms
         * ====消耗时间为=========18ms
         */
        Directory directory = FSDirectory.open(Paths.get(IndexDir));
        //5. 创建输入流对象
        IndexReader reader = DirectoryReader.open(directory);
        //6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //7. 搜索并获取搜索结果
        BooleanQuery build = builder.build();
        TopDocs topDocs = indexSearcher.search(build, end);
        //8. 获取查询到的总条数
        resultModel.setTotalRow(topDocs.totalHits.value);
        //9. 获取查询到的结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        long endTime = System.currentTimeMillis();
        System.out.println("====消耗时间为=========" + (endTime - startTime) + "ms");
        //10. 遍历结果集封装返回的数据

        //11. 高亮
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"color:red\">", "</span>");
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(build));

        List<Blog> skuList = new ArrayList<Blog>();
        if (scoreDocs != null) {
            for (int i = start; i < scoreDocs.length; i ++) {
                //通过查询到的文档编号, 找到对应的文档对象
                int id = scoreDocs[i].doc;
                Document document = reader.document(id);
                String content = document.get("content");
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), id, "content", analyzer);
                TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, content, false, 10);
                for (int j = 0; j < frag.length; j++) {
                    if ((frag[j] != null) && (frag[j].getScore() > 0)) {
                        content = frag[j].toString();
                        log.info("高亮词: {}",content);
                    }
                }

                //封装对象
                Blog obj = new Blog();
                obj.setContent(content);
                obj.setId(Integer.parseInt(document.get("id")));
                obj.setCategory(document.get("category"));
                obj.setAuthorAge((Integer.parseInt(document.get("authorAge"))));
//                obj.setStartArticle((Integer.parseInt(document.get("startArticle"))));
                skuList.add(obj);
            }
        }
        //封装查询到的结果集
        resultModel.setList(skuList);
        //封装当前页
        resultModel.setPageNumber(page);
        resultModel.setPageSize(PAGE_SIZE);
        //总页数
        Long pageCount = topDocs.totalHits.value % PAGE_SIZE > 0 ? (topDocs.totalHits.value/PAGE_SIZE) + 1 : topDocs.totalHits.value/PAGE_SIZE;
        resultModel.setTotalPage(pageCount);
        return resultModel;
    }

    @Override
    public void createFullIndex() throws Exception  {
        List<Blog> all = service.getAll();
        List<Document> docList = new ArrayList<>();
        for(Blog sku : all){
            //2. 创建文档对象
            Document document = new Document();

            //创建域对象并且放入文档对象中
            /**
             * 是否分词: 否, 因为主键分词后无意义
             * 是否索引: 是, 如果根据id主键查询, 就必须索引
             * 是否存储: 是, 因为主键id比较特殊, 可以确定唯一的一条数据, 在业务上一般有重要所用, 所以存储
             *      存储后, 才可以获取到id具体的内容
             */
            document.add(new StringField("id", sku.getId()+"", Field.Store.YES));
            document.add(new StringField("category", sku.getCategory(), Field.Store.YES));
            document.add(new TextField("content", sku.getContent(), Field.Store.YES));

            document.add(new IntPoint("authorAge", sku.getAuthorAge()));
            document.add(new StoredField("authorAge", sku.getAuthorAge()));

            document.add(new NumericDocValuesField("startArticle", sku.getStartArticle().longValue()));

            docList.add(document);
        }

        //3. 创建分词器, StandardAnalyzer标准分词器, 对英文分词效果好, 对中文是单字分词, 也就是一个字就认为是一个词.
        Analyzer analyzer = new IKAnalyzer();
        //4. 创建Directory目录对象, 目录对象表示索引库的位置
        Directory  dir = FSDirectory.open(Paths.get(IndexDir));
        //5. 创建IndexWriterConfig对象, 这个对象中指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //6. 创建IndexWriter输出流对象, 指定输出的位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(dir, config);
        //7. 写入文档到索引库
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        //8. 释放资源
        indexWriter.close();
    }

    @Override
    public void delTermIndex(String id) throws Exception {
        //3. 创建分词器, StandardAnalyzer标准分词器, 对英文分词效果好, 对中文是单字分词, 也就是一个字就认为是一个词.
        Analyzer analyzer = new StandardAnalyzer();
        //4. 创建Directory目录对象, 目录对象表示索引库的位置
        Directory  dir = FSDirectory.open(Paths.get(IndexDir));
        //5. 创建IndexWriterConfig对象, 这个对象中指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //6. 创建IndexWriter输出流对象, 指定输出的位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(dir, config);
        //测试根据条件删除
        indexWriter.deleteDocuments(new Term("id", id));
        //8. 释放资源
        indexWriter.close();
    }
}
