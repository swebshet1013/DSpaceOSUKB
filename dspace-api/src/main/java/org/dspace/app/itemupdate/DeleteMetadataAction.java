/*
 * DeleteMetadataAction.java
 *
 * Version: $Revision: 3984 $
 *
 * Date: $Date: 2009-06-29 22:33:25 -0400 (Mon, 29 Jun 2009) $
 *
 * Copyright (c) 2002-2009, The DSpace Foundation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the DSpace Foundation nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.dspace.app.itemupdate;

import java.text.ParseException;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.Context;

/**
 *    Action to delete metadata 
 * 
 *
 */
public class DeleteMetadataAction extends UpdateMetadataAction {

	/**
	 *   Delete metadata from item
	 * 
	 *  @param context
	 *  @param itarch
	 *  @param isTest
	 *  @param suppressUndo
	 *  @throws ParseException
	 *  @throws AuthorizeException
	 */
	public void execute(Context context, ItemArchive itarch, boolean isTest,
            boolean suppressUndo) throws AuthorizeException, ParseException 
	{
		Item item = itarch.getItem();
		for (String f : targetFields)
		{
			DtoMetadata dummy = DtoMetadata.create(f, Item.ANY, "");
			DCValue[] ardcv = item.getMetadata(f);

			ItemUpdate.pr("Metadata to be deleted: ");
			for (DCValue dcv : ardcv)
			{
				ItemUpdate.pr("  " + MetadataUtilities.getDCValueString(dcv));
			}

			if (!isTest)
			{
				if (!suppressUndo)
				{
					for (DCValue dcv : ardcv)
					{
						itarch.addUndoMetadataField(DtoMetadata.create(dcv.schema, dcv.element, 
								dcv.qualifier, dcv.language, dcv.value));					
					}
				}
				
				item.clearMetadata(dummy.schema, dummy.element, dummy.qualifier, Item.ANY);	
			}
		}
	}
}
