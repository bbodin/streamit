/*
 * Copyright 2003 by the Massachusetts Institute of Technology.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for any purpose and without
 * fee is hereby granted, provided that the above copyright
 * notice appear in all copies and that both that copyright
 * notice and this permission notice appear in supporting
 * documentation, and that the name of M.I.T. not be used in
 * advertising or publicity pertaining to distribution of the
 * software without specific, written prior permission.
 * M.I.T. makes no representations about the suitability of
 * this software for any purpose.  It is provided "as is"
 * without express or implied warranty.
 */

package streamit.scheduler2.iriter;

/**
 * An interface for retrieving data about streams with a Splitter and
 * a Joiner.
 * 
 * @version 2
 * @author  Michal Karczmarek
 */

public interface SplitterNJoinerIter extends SplitterIter, JoinerIter
{
    /**
     * Returns an Iterator that pointst to the same object as this 
     * specialized iterator.
     * @return an Iterator that points to the same object
     */
    public Iterator getUnspecializedIter();
}
