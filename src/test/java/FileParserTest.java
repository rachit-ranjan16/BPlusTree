import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.ExpectedExceptionsHolder;

public class FileParserTest {

    private FileParser fp;
    private FileParser noOrderFp;
    @BeforeClass
    public void init() {
        fp = new FileParser("src/test/resources/InputFile");
        noOrderFp = new FileParser("src/test/resources/NoOrderInputFile");
    }

    @Test
    public void testOrderRead() throws IllegalArgumentException {
        fp.readFileContent();
        Assert.assertEquals(fp.getOrder(), 12);
    }

//    @Test
//    public void testOrderReadWithNonIntegerOrderInputFile() throws IllegalArgumentException {
//        noOrderFp.readFileContent();
//        Assert.assertEquals(true, true);
//    }
}
