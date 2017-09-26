package pl.psnc.indigo.cli;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;

@Category(UnitTests.class)
public class IndigoClientTest {
    @Test
    public final void main() throws Exception {
        IndigoClient.main(new String[]{"-help"});
        IndigoClient.main(new String[]{"-verbose"});
    }
}
