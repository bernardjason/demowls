package fml;

import java.io.*;
import java.lang.*;
import java.util.*;
import weblogic.wtc.jatmi.*;

public final class flds
	implements weblogic.wtc.jatmi.FldTbl
{
	Hashtable nametofieldHashTable;
	Hashtable fieldtonameHashTable;
	/** number: 100  type: string */
	public final static int EMAIL = 167772260;
	/** number: 101  type: string */
	public final static int SUBJECT = 167772261;
	/** number: 102  type: string */
	public final static int MESSAGE = 167772262;

	public String Fldid_to_name(int fldid)
	{
		if ( fieldtonameHashTable == null ) {
			fieldtonameHashTable = new Hashtable();
			fieldtonameHashTable.put(new Integer(EMAIL), "EMAIL");
			fieldtonameHashTable.put(new Integer(SUBJECT), "SUBJECT");
			fieldtonameHashTable.put(new Integer(MESSAGE), "MESSAGE");
		}

		return ((String)fieldtonameHashTable.get(new Integer(fldid)));
	}

	public int name_to_Fldid(String name)
	{
		if ( nametofieldHashTable == null ) {
			nametofieldHashTable = new Hashtable();
			nametofieldHashTable.put("EMAIL", new Integer(EMAIL));
			nametofieldHashTable.put("SUBJECT", new Integer(SUBJECT));
			nametofieldHashTable.put("MESSAGE", new Integer(MESSAGE));
		}

		Integer fld = (Integer)nametofieldHashTable.get(name);
		if (fld == null) {
			return (-1);
		} else {
			return (fld.intValue());
		}
	}

	public String[] getFldNames()
	{
		String retval[] = new String[3];
		retval[0] = new String("EMAIL");
		retval[1] = new String("SUBJECT");
		retval[2] = new String("MESSAGE");
		return retval;
	}
}
