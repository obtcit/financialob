/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package tw.com.obtc.financialob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static tw.com.obtc.financialob.db.DatabaseHelper.PAYEE_TABLE;
import static tw.com.obtc.orb.EntityManager.DEF_SORT_COL;

@Entity
@Table(name = PAYEE_TABLE)
public class Payee extends MyEntity implements SortableEntity {

    public static final Payee EMPTY = new Payee();

    static {
        EMPTY.id = 0;
        EMPTY.title = "No payee";
    }

    @Column(name = "last_category_id")
    public long lastCategoryId;

    @Column(name = DEF_SORT_COL)
    public long sortOrder;

    @Override
    public long getSortOrder() {
        return sortOrder;
    }
}
