import com.dhais.TestCommonApplication;
import com.dhais.service.IUserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ibase4j.core.util.SecurityUtil;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/3/11 15:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestCommonApplication.class)
public class Test {

    @Autowired
    private IUserService service;

    @org.junit.Test
    public void test1(){
        System.out.println(SecurityUtil.encryptDes("root"));
        System.out.println(SecurityUtil.decryptDes("faijBM35E5y="));
    }

    @org.junit.Test
    public void test2(){
        service.testDefaultDataSource();
    }
}
