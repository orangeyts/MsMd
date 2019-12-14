package lucene;

/**
 * lucene 实例
 * 下载发行包 https://mirrors.tuna.tsinghua.edu.cn/apache/lucene/java/8.3.1/lucene-8.3.1.zip
 * 1 把下面四个包放在解压的 queryparser 目录下
 * lucene-8.3.1\core\lucene-core-8.3.1.jar
 * lucene-8.3.1\queryparser\lucene-queryparser-8.3.1.jar
 * lucene-8.3.1\analysis\common\lucene-analyzers-common-8.3.1.jar
 * lucene-8.3.1\demo\lucene-demo-8.3.1.jar
 * java -cp .;lucene-core-8.3.1.jar;lucene-queryparser-8.3.1.jar;lucene-analyzers-common-8.3.1.jar;lucene-demo-8.3.1.jar org.apache.lucene.demo.IndexFiles -docs .
 * -docs 是要被索引的文件目录, . 代表当前目录,默认名称是index
 * java -cp .;lucene-core-8.3.1.jar;lucene-queryparser-8.3.1.jar;lucene-analyzers-common-8.3.1.jar;lucene-demo-8.3.1.jar org.apache.lucene.demo.SearchFiles
 */
public class Demo {
}
