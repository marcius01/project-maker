package tech.skullprogrammer.projectmaker.model.operator.chain;

import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JCodeModel;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainFactory {

    private static Logger logger = LoggerFactory.getLogger(ChainFactory.class);
    private static String UNIQUE_SUFFIX = "UNIQUE";
    private static String DATE_SUFFIX = "DATE";
    private static String TIMESTAMP_SUFFIX = "TIMESTAMP";
    private static String ID_SUFFIX = "ID";
    private static final JCodeModel jCodeModel = new JCodeModel();

    public static IChain generateNameChain() {
        IChain rootChain = new NameChain(UNIQUE_SUFFIX);
        IChain idChain = new NameChain(ID_SUFFIX);
        IChain dateChain = new NameChain(DATE_SUFFIX);
        IChain timestampChain = new NameChain(TIMESTAMP_SUFFIX);
        rootChain.setNextStep(idChain);
        idChain.setNextStep(dateChain);
        dateChain.setNextStep(timestampChain);
        return rootChain;
    }

    public static IChain generateMethodChain() {
        IChain rootChain = new MethodChain(UNIQUE_SUFFIX, ImmutableMap.of(Column.class, new ImmutablePair<>("unique", Boolean.TRUE)), jCodeModel);    
        IChain idChain = new MethodChain(ID_SUFFIX, ImmutableMap.of(Id.class, new ImmutablePair<>(null, null), GeneratedValue.class, new ImmutablePair<>("strategy", GenerationType.TABLE)), jCodeModel);        
        IChain dateChain = new MethodChain(DATE_SUFFIX, ImmutableMap.of(Temporal.class, new ImmutablePair<>("value", TemporalType.DATE)), jCodeModel);        
        IChain timestampChain = new MethodChain(TIMESTAMP_SUFFIX, ImmutableMap.of(Temporal.class, new ImmutablePair<>("value", TemporalType.TIMESTAMP)), jCodeModel);        
        rootChain.setNextStep(idChain);
        idChain.setNextStep(dateChain);
        dateChain.setNextStep(timestampChain);
        return rootChain;
    }

}
