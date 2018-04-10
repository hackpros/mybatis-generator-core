package org.mybatis.generator.codegen.mybatis3.control;

import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;


/**
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public abstract class AbstractJavaControlGenerator extends AbstractJavaGenerator {

    private boolean requiresXMLGenerator;
    
    public AbstractJavaControlGenerator(boolean requiresXMLGenerator) {
        super();
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    /**
     * @return true if matching XML is required
     */
    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }
    
    /**
     * This method returns an instance of the XML generator associated
     * with this client generator.
     * 
     * @return the matched XML generator.  May return null if no
     * XML is required by this generator
     */
    public abstract AbstractXmlGenerator getMatchedXMLGenerator();

    public abstract void addMethod(TopLevelClass topLevelClass);
    
    public abstract void init();
}
