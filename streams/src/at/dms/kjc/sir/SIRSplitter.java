package at.dms.kjc.sir;

import at.dms.kjc.*;
import at.dms.util.*;

/**
 * This represents a stream construct with a single input and multiple
 * outputs.
 */
public class SIRSplitter extends SIROperator {
    /** 
     * The type of this joiner.
     */
    private SIRSplitType type;
    /** 
     * The number of items that the splitter pushes to each output tape
     * in one execution cycle.
     */
    private JExpression[] weights;

    private SIRSplitter(SIRContainer parent, 
			SIRSplitType type, 
			JExpression[] weights) {
      super(parent);
      this.weights = weights;
      this.type = type;
    }

    /**
     * Constructs a splitter with given parent, type and n number of inputs.
     */
    public static SIRSplitter create(SIRContainer parent, 
				     SIRSplitType type, 
				     int n) {
	if (type==SIRSplitType.ROUND_ROBIN || type==SIRSplitType.DUPLICATE) {
	    // fill weights with 1
	    return new SIRSplitter(parent, type, initLiteralArray(n, 1));
        } else if (type==SIRSplitType.NULL) {
	    // for null type, fill with zero weights
	    return new SIRSplitter(parent, type, initLiteralArray(n, 0));
	} else if (type==SIRSplitType.WEIGHTED_RR) {
	    // if making a weighted round robin, should use other constructor
	    fail("Need to specify weights for weighted round robin");
	} else {
	    fail("Unreckognized splitter type.");
	}
	// stupid compiler
	return null;
    }

    /**
     * Constructs a weighted round-robin splitter with the given
     * parent and weights.  
     */
    public static SIRSplitter createWeightedRR(SIRContainer parent, 
					       JExpression[] weights) {
	return new SIRSplitter(parent, SIRSplitType.WEIGHTED_RR, weights);
    }

    /**
     * Tests whether or not this has the same type and the same
     * weights as obj.  This can return true for splitters with
     * different numbers of outputs if the type is not a weighted
     * round robin.
     */
    public boolean equals(SIRSplitter obj) {
	if (obj.type!=SIRSplitType.WEIGHTED_RR || 
	        type!=SIRSplitType.WEIGHTED_RR) {
	    return type==obj.type;
	} else {
	    return equalArrays(getWeights(), obj.getWeights());
	}
    }

    /**
     * If this is a splitter that has equal weight per way, then
     * rescale the weights to be of the given <extent>
     */
    public void rescale(int extent) {
	if (type==SIRSplitType.DUPLICATE ||
	    type==SIRSplitType.ROUND_ROBIN ||
	    type==SIRSplitType.NULL) {
	    this.weights = initLiteralArray(extent, ((JIntLiteral)
						     weights[0]).intValue());
	}
    }
    
    /**
     * Accepts visitor <v> at this node.
     */
    public void accept(StreamVisitor v) {
	v.visitSplitter(this,
			parent,
			type,
			weights);
    }

    /**
     * Accepts attribute visitor <v> at this node.
     */
    public Object accept(AttributeStreamVisitor v) {
	return v.visitSplitter(this,
			       parent,
			       type,
			       weights);
    }

    /**
     * Return type of this.
     */
    public SIRSplitType getType() {
	return type;
    }

    /**
     * Return the number of outputs of this.
     */
    public int getWays() {
	return weights.length;
    }

    /**
     * Returns JExpression weights of this.
     */
    public JExpression[] getInternalWeights() {
	return weights;
    }

    /**
     * Return int weights array of this.
     */
    public int[] getWeights() {
	int[] result = new int[weights.length];
	for (int i=0; i<weights.length; i++) {
	    Utils.assert(weights[i] instanceof JIntLiteral,
			 "Expecting JIntLiteral as weight to round-robin--" +
			 "could have problems with constant prop (maybe " +
			 "it hasn't been run yet) or orig program");
	    result[i] = ((JIntLiteral)weights[i]).intValue();
	}
	return result;
    }
}
