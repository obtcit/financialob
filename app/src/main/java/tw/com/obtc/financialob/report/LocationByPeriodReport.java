package tw.com.obtc.financialob.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.db.MyEntityManager;
import tw.com.obtc.financialob.db.DatabaseHelper.TransactionColumns;
import tw.com.obtc.financialob.graph.Report2DChart;
import tw.com.obtc.financialob.model.Currency;
import tw.com.obtc.financialob.model.MyLocation;
import tw.com.obtc.financialob.utils.MyPreferences;

import android.content.Context;

/**
 * 2D Chart Report to display monthly results by Locations.
 *
 * @author Abdsandryk
 */
public class LocationByPeriodReport extends Report2DChart {

    public LocationByPeriodReport(Context context, MyEntityManager em, Calendar startPeriod, int periodLength, Currency currency) {
        super(context, em, startPeriod, periodLength, currency);
    }

    @Override
    public String getFilterName() {
        if (filterIds.size() > 0) {
            long locationId = filterIds.get(currentFilterOrder);
            MyLocation location = em.get(MyLocation.class, locationId);
            if (location != null) {
                return location.title;
            } else {
                return context.getString(R.string.current_location);
            }
        } else {
            // no location
            return context.getString(R.string.current_location);
        }
    }

    /* (non-Javadoc)
     * @see tw.com.obtc.financialob.graph.ReportGraphic2D#setFilterIds()
     */
    @Override
    public void setFilterIds() {
        boolean includeNoLocation = MyPreferences.includeNoFilterInReport(context);
        filterIds = new ArrayList<Long>();
        currentFilterOrder = 0;
        List<MyLocation> locations = em.getAllLocationsList(includeNoLocation);
        if (locations.size() > 0) {
            MyLocation l;
            for (int i = 0; i < locations.size(); i++) {
                l = locations.get(i);
                filterIds.add(l.id);
            }
        }
    }

    @Override
    protected void setColumnFilter() {
        columnFilter = TransactionColumns.location_id.name();
    }

    @Override
    public String getNoFilterMessage(Context context) {
        return context.getString(R.string.report_no_location);
    }

}
