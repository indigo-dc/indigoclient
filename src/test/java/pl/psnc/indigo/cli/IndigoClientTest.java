package pl.psnc.indigo.cli;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.cli.category.UnitTests;

@Category(UnitTests.class)
public class IndigoClientTest {

    @Test
    public final void testMain() throws Exception {
      String [] args = {"hello"};
      IndigoClient.main(args);
    }
}
