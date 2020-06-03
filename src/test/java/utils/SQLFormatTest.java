package utils;

import com.demo.util.MyBatisFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class SQLFormatTest {

    @Test
    public void test() throws IOException {
        String sql = "Preparing: SELECT DISTINCT temp1.*,e.title,d.relation_title AS parent FROM (SELECT temp.* ,d.created AS leaveTime ,d.parent_id,d.resign AS leaveResign FROM(SELECT a.class_id,a.id,a.name,e.created AS arriveTime ,e.resign AS arriveResign FROM tb_student a LEFT JOIN ( SELECT MIN( b.created ) AS created, b.student_id,b.resign FROM (SELECT p.* FROM tb_student s,tb_student_detection p WHERE s.class_id=? AND p.student_id=s.id) as b WHERE DATE_FORMAT( b.created, '%Y-%m-%d' ) = ? GROUP BY student_id )e ON e.student_id = a.id WHERE a.class_id=? AND a.deleted=0 ) AS temp LEFT JOIN ( SELECT MAX( c.created ) AS created ,c.parent_id,c.student_id,c.resign FROM (SELECT p.* FROM tb_student s,tb_student_away_record p WHERE s.class_id=? AND p.student_id=s.id) as c WHERE DATE_FORMAT( c.created, '%Y-%m-%d' ) = ? GROUP BY student_id ) d ON d.student_id = temp.id ) AS temp1 LEFT JOIN tb_student_parent d ON temp1.id = d.student_id AND d.parent_id=temp1.parent_id LEFT JOIN tb_class e ON temp1.class_id = e.id \n" +
                " ==> Parameters: 4269(Integer), 2020-06-03(String), 4269(Integer), 4269(Integer), 2020-06-03(String)";
        String format = MyBatisFormatter.format(sql);
        System.out.println(format);
    }
    @Test
    public void test11() throws IOException {
        String sql = "Preparing: SELECT tb_student_away_record.*, a.file_path AS filePath ,b.file_path AS fileSubPath FROM tb_student_away_record LEFT JOIN tb_file a ON tb_student_away_record.file_img_id = a.id LEFT JOIN tb_file b ON tb_student_away_record.file_sub_img_id = b.id WHERE tb_student_away_record.student_id = ? AND DATE_FORMAT(tb_student_away_record.created,'%Y-%m-%d') = ? ORDER BY tb_student_away_record.created \n" +
                "-| ==> Parameters: 1(Integer), 2020-01-03(String)";
        String format = MyBatisFormatter.format(sql);
        System.out.println(format);
    }

    public static void main(String[] args) {
        String input = "123(Long)";
        String regex = "\\(([^}]*)\\)";
//        String regex = "\\{([^}]*)\\}";//匹配大括号
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
