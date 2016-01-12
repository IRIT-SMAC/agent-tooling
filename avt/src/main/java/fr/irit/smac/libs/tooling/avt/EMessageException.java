package fr.irit.smac.libs.tooling.avt;

/**
 * The Enum EMessageException.
 */
public enum EMessageException {

    /** The lower bound nan. */
    LOWER_BOUND_NAN("lowerBound isNaN"),
    
    /** The upper bound nan. */
    UPPER_BOUND_NAN("upperBound isNaN"),
    
    /** The start value nan. */
    START_VALUE_NAN("startValue isNaN"),
    
    /** The lower bound gt upper bound. */
    LOWER_BOUND_GT_UPPER_BOUND("lowerBound > upperBound"),
    
    /** The delta manager factory null. */
    DELTA_MANAGER_FACTORY_NULL("deltaManagerFactory == null"),
    
    /** The value nan. */
    VALUE_NAN("value isNaN"),
    
    /** The feedback null. */
    FEEDBACK_NULL("feedback is null"),
    
    /** The criticity nan. */
    CRITICITY_NAN("criticity isNaN"),
    
    /** The CRITICIT y_ l t_0. */
    CRITICITY_LT_0("criticity < 0"),
    
    /** The CRITICIT y_ g t_1. */
    CRITICITY_GT_1("criticity > 1"),
    
    /** The delta min nan. */
    DELTA_MIN_NAN("deltaMin isNaN"),
    
    /** The range null. */
    RANGE_NULL("range is null"),
    
    /** The DELT a_ mi n_ l e_0. */
    DELTA_MIN_LE_0("deltaMin <= 0"),
    
    /** The delta evolution null. */
    DELTA_EVOLUTION_NULL("deltaEvolution == null"),
    
    /** The DELT a_ evolutio n_ increas e_ facto r_ l e_1. */
    DELTA_EVOLUTION_INCREASE_FACTOR_LE_1("deltaEvolution.getIncreaseFactor() <= 1."),
    
    /** The delta max nan. */
    DELTA_MAX_NAN("deltaMax isNaN"),
    
    /** The delta max lt delta min. */
    DELTA_MAX_LT_DELTA_MIN("deltaMax < deltaMin"),
    
    /** The delta min gt delta max. */
    DELTA_MIN_GT_DELTA_MAX("deltaMin > this.deltaMax"),
    
    /** The direction null. */
    DIRECTION_NULL("direction is null"),
    
    /** The delta nan. */
    DELTA_NAN("delta isNaN"),
    
    /** The delta max lt this delta min. */
    DELTA_MAX_LT_THIS_DELTA_MIN("deltaMax < this.deltaMin"),
    
    /** The geometric factor bound nan. */
    GEOMETRIC_FACTOR_BOUND_NAN("geometricFactor isNaN"),
    
    /** The delta min ge range. */
    DELTA_MIN_GE_RANGE("deltaMin => range"),
    
    /** The RANG e_ g t_ ma x_ valu e_ tim e_2. */
    RANGE_GT_MAX_VALUE_TIME_2("range > Double.MAX_VALUE * 2"),
    
    /** The geometric step number bound. */
    GEOMETRIC_STEP_NUMBER_BOUND("if geometricStepNumber < 1 or geometricStepNumber > this.getNbGeometricSteps()");

    /** The text. */
    private String text;

    /**
     * Instantiates a new e message exception.
     *
     * @param text the text
     */
    private EMessageException(String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

}
