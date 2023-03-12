package tw.com.obtc.financialob.export.qif;

import tw.com.obtc.financialob.export.CategoryInfo;
import tw.com.obtc.financialob.model.Category;

import java.io.IOException;

import static tw.com.obtc.financialob.export.qif.QifUtils.trimFirstChar;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 2/16/11 10:08 PM
 */
public class QifCategory extends CategoryInfo {

    public QifCategory() {
    }

    public QifCategory(String name, boolean income) {
        super(name, income);
    }

    public static QifCategory fromCategory(Category c) {
        QifCategory qifCategory = new QifCategory();
        qifCategory.name = buildName(c);
        qifCategory.isIncome = c.isIncome();
        return qifCategory;
    }

    public void writeTo(QifBufferedWriter qifWriter) throws IOException {
        qifWriter.write("N").write(name).newLine();
        qifWriter.write(isIncome ? "I" : "E").newLine();
        qifWriter.end();
    }

    public void readFrom(QifBufferedReader r) throws IOException {
        String line;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("^")) {
                break;
            }
            if (line.startsWith("N")) {
                this.name = trimFirstChar(line);
            } else if (line.startsWith("I")) {
                this.isIncome = true;
            }
        }
    }

}
