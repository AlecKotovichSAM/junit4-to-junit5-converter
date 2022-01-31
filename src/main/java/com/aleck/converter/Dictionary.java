/**
 * 
 */
package com.aleck.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alec K
 *
 */
public class Dictionary {

    public static final Map<String, String> TOKENS = Collections.unmodifiableMap(new HashMap<>() {
        {
            put("import org.junit.Before;", "import org.junit.jupiter.api.BeforeEach;");
            put("import org.junit.After;", "import org.junit.jupiter.api.AfterEach;");
            put("import org.junit.BeforeClass;", "import org.junit.jupiter.api.BeforeAll;");
            put("import org.junit.AfterClass;", "import org.junit.jupiter.api.AfterAll;");
            put("import org.junit.Ignore;", "import org.junit.jupiter.api.Disabled;");
            put("import org.junit.Test;", "import org.junit.jupiter.api.Test;");
            put("import org.junit.runner.RunWith;", "import org.junit.jupiter.api.extension.ExtendWith;");
            put("import org.junit.Assert;", "import org.junit.jupiter.api.Assertions;");
            put("import org.junit.Assume;", "import org.junit.jupiter.api.Assertions;");
            
            put("import org.junit.Assert.*;", "import org.junit.jupiter.api.Assertions.*;");
            put("import static org.junit.Assert.assertEquals;", "import static org.junit.jupiter.api.Assertions.assertEquals;");
            put("import static org.junit.Assert.assertNotEquals;", "import static org.junit.jupiter.api.Assertions.assertNotEquals;");
            put("import static org.junit.Assert.assertNotNull;", "import static org.junit.jupiter.api.Assertions.assertNotNull;");
            put("import static org.junit.Assert.assertNull;", "import static org.junit.jupiter.api.Assertions.assertNull;");
            put("import static org.junit.Assert.assertTrue;", "import static org.junit.jupiter.api.Assertions.assertTrue;");
            put("import static org.junit.Assert.assertFalse;", "import static org.junit.jupiter.api.Assertions.assertFalse;");
            

            put("import org.springframework.test.context.junit4.SpringRunner;",
                    "import org.springframework.test.context.junit.jupiter.SpringExtension;");
            //put("@RunWith(SpringRunner.class)", "@ExtendWith(SpringExtension.class)");
            put("@RunWith(SpringRunner.class)", "");
            put("@RunWith(MockitoJUnitRunner.class)", "@ExtendWith(MockitoExtension.class)");
            put("@Before", "@BeforeEach");
            put("@After", "@AfterEach");
            put("@BeforeClass", "@BeforeAll");
            put("@AfterClass", "@AfterAll");
            put("@Ignore", "@Disabled");
                       
            put("Assert.assertNotNull", "Assertions.assertNotNull");
            put("Assert.assertEquals", "Assertions.assertEquals");
            put("Assert.fail", "Assertions.fail");
            
        }
    });
}
