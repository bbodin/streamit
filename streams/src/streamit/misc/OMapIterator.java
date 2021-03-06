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

package streamit.misc;

public class OMapIterator
{
    OSetIterator setIter;

    OMapIterator(OSetIterator _setIter)
    {
        assert _setIter != null;

        setIter = _setIter;
    }

    public Object getKey()
    {
        return ((Pair)setIter.get()).first;
    }

    public Object getData()
    {
        return ((Pair)setIter.get()).second;
    }
    
    /**
     * Overwrite the data mapped to a certain key. Returns the old object
     * mapped to the key.
     */
    public Object setData(Object newData)
    {
        Pair mapPair = (Pair)setIter.get(); 
        Object oldData = mapPair.second;
        mapPair.second = newData;
        
        return oldData;
    }

    public void next()
    {
        setIter.next();
    }

    public void prev()
    {
        setIter.prev();
    }

    public OMapIterator copy()
    {
        return new OMapIterator(setIter);
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof OMapIterator))
            return false;

        return (setIter.equals(((OMapIterator)other).setIter));
    }
}
