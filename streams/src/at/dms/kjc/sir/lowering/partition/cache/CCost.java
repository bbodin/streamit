package at.dms.kjc.sir.lowering.partition.cache;

class CCost {
    /**
     * The cost of a single fused partition or a set of partitions
     * including various fusion overheads.
     */
    private final long cost;

    public CCost(long cost) {
        this.cost = cost;
    }

    public long getCost() {
        return cost;
    }

    /**
     * Returns a conceptual "maximum cost" that will be greater than
     * everything.
     */
    public static CCost MAX_VALUE() {
        return new CCost(Long.MAX_VALUE/2-1);
    }

    /**
     * Returns whether or not this is greather than <pre>other</pre>.
     */
    public boolean greaterThan(CCost other) {
        return this.cost > other.cost;
    }

    /**
     * Returns the combined cost of <pre>cost1</pre> and <pre>cost2</pre>, if they are
     * running in different partitions (that is, running in parallel
     * partitions, either in a splitjoin or pipeline).
     */
    public static CCost combine(CCost cost1, CCost cost2) {
        long cost = cost1.getCost() + cost2.getCost();

        // keep within range so that we can add again
        if (cost > Long.MAX_VALUE/2-1) {
            cost = Long.MAX_VALUE/2-1;
        }

        return new CCost(cost);
    }

    /**
     * Returns the sum of <pre>cost1</pre> and <pre>cost2</pre>, for the case in which
     * they are running in the same partition.  Includes a cost of
     * some fusion overhead.
     */

    /*
      public static CCost add(CCost cost1, CCost cost2) {
      // first just take sums
      int cost = cost1.getCost() + cost2.getCost();
      int iCode = cost1.getICodeSize() + cost2.getICodeSize();
    
      // if iCode threshold is exceeded, then bump up the other
      // costs.
      if (iCode > CachePartitioner.ICODE_THRESHOLD) {
      cost = Long.MAX_VALUE/2-1;
      }

      return new CCost(cost, iCode);
      }
    */

    public boolean equals(Object o) {
        if (!(o instanceof CCost)) {
            return false;
        }
        CCost other = (CCost)o;
        return other.cost==this.cost;
    }

    public String toString() {
        return "[cost=" + cost + "]";
    }
}
