package tw.com.obtc.financialob.report;

import tw.com.obtc.financialob.graph.GraphUnit;
import tw.com.obtc.financialob.model.Total;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 2/28/11 9:16 PM
 */
public class ReportData {

    public final List<GraphUnit> units;
    public final Total total;

    public ReportData(List<GraphUnit> units, Total total) {
        this.units = units;
        this.total = total;
    }

}
