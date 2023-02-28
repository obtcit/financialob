package tw.com.obtc.financialob.report;

import java.util.ArrayList;
import java.util.Calendar;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.db.MyEntityManager;
import tw.com.obtc.financialob.db.DatabaseHelper.TransactionColumns;
import tw.com.obtc.financialob.graph.Report2DChart;
import tw.com.obtc.financialob.model.Currency;
import tw.com.obtc.financialob.model.Project;
import tw.com.obtc.financialob.utils.MyPreferences;

import android.content.Context;

/**
 * 2D Chart Report to display monthly results by Projects.
 *
 * @author Abdsandryk
 */
public class ProjectByPeriodReport extends Report2DChart {

    public ProjectByPeriodReport(Context context, MyEntityManager em, Calendar startPeriod, int periodLength, Currency currency) {
        super(context, em, startPeriod, periodLength, currency);
    }

    /* (non-Javadoc)
     * @see tw.com.obtc.financialob.graph.ReportGraphic2D#getFilterName()
     */
    @Override
    public String getFilterName() {
        if (filterIds.size() > 0) {
            long projectId = filterIds.get(currentFilterOrder);
            Project project = em.getProject(projectId);
            if (project != null) {
                return project.getTitle();
            } else {
                return context.getString(R.string.no_project);
            }
        } else {
            // no project
            return context.getString(R.string.no_project);
        }
    }

    @Override
    public void setFilterIds() {
        boolean includeNoProject = MyPreferences.includeNoFilterInReport(context);
        filterIds = new ArrayList<Long>();
        currentFilterOrder = 0;
        ArrayList<Project> projects = em.getAllProjectsList(includeNoProject);
        if (projects.size() > 0) {
            Project p;
            for (int i = 0; i < projects.size(); i++) {
                p = projects.get(i);
                filterIds.add(p.getId());
            }
        }
    }

    @Override
    protected void setColumnFilter() {
        columnFilter = TransactionColumns.project_id.name();
    }

    @Override
    public String getNoFilterMessage(Context context) {
        return context.getString(R.string.report_no_project);
    }
}
