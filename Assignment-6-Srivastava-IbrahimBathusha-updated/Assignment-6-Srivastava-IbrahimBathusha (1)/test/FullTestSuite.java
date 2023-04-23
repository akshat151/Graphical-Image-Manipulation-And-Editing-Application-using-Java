import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import helpers.ImageImpHelperTest;
import imagecontroller.ImageControllerTest;
import imagemodel.ImageImplTest;
import imageview.TerminalViewTest;

/**
 * Test suite running all the individual test sets.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
      ImageImpHelperTest.class,
      ImageImplTest.class,
      ImageControllerTest.class,
      TerminalViewTest.class
})
public class FullTestSuite {
}
