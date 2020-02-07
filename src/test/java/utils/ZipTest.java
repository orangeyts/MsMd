package utils;

import com.demo.util.ZipUtils;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ZipTest {
    @Test
    public void folder() throws FileNotFoundException {
        //第一个参数是需要压缩的源路径；第二个参数是压缩文件的目的路径，这边需要将压缩的文件名字加上去
        ZipUtils.compress("E:\\ciHome\\h5\\dist","E:\\ciHome\\h5\\dist\\dist.zip");
    }

    /**
     * 压缩一个文件夹下，可选择的几个文件
     * @throws FileNotFoundException
     */
    @Test
    public void multiFolder() throws Exception {
        //第一个参数是需要压缩的源路径；第二个参数是压缩文件的目的路径，这边需要将压缩的文件名字加上去
        ZipUtils.compressMultiFolder("E:\\ciHome\\h5\\dist","E:\\ciHome\\h5\\dist\\web.zip","index.html","asset");
    }
}
