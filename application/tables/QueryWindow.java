package tables;

import code.Query;
import code.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static code.Main.errorColor;
import static java.lang.System.exit;

public class QueryWindow {

    public JPanel mainPanel;
    //code.Query 4
    protected JTable table_QUERY4;
    protected JButton search_QUERY4;
    protected JLabel response_QUERY4;
    protected JTextField param_QUERY4_0;
    //code.Query 1
    protected JTextField param_QUERY1_1;
    protected JTextField param_QUERY1_0;
    protected JButton search_QUERY1;
    protected JTable table_QUERY1;
    protected JLabel response_QUERY1;
    //code.Query 2
    protected JButton search_QUERY2;
    protected JLabel response_QUERY2;
    protected JTable table_QUERY2;
    //code.Query 3
    protected JButton search_QUERY3;
    protected JLabel response_QUERY3;
    protected JTable table_QUERY3;
    //code.Query 5
    protected JTextField param_QUERY5_0;
    protected JButton search_QUERY5;
    protected JTable table_QUERY5;
    protected JLabel response_QUERY5;
    //code.Query 6
    protected JTable table_QUERY6;
    protected JButton search_QUERY6;
    protected JLabel response_QUERY6;
    protected JTextField param_QUERY6_0;
    //code.Query 7
    protected JTable table_QUERY7;
    protected JButton search_QUERY7;
    protected JLabel response_QUERY7;
    protected JTextField param_QUERY7_0;
    protected JTextField param_QUERY7_1;
    //code.Query 8
    protected JTextField param_QUERY8_0;
    protected JTextField param_QUERY8_1;
    protected JButton search_QUERY8;
    protected JTable table_QUERY8;
    protected JLabel response_QUERY8;
    //code.Query 9
    protected JTextField param_QUERY9_0;
    protected JButton search_QUERY9;
    protected JTable table_QUERY9;
    protected JLabel response_QUERY9;

    static final int noQueries = Query.implementedQueries.count;
    static final Query.implementedQueries[] queryNames = Query.implementedQueries.values();

    private final Query[] queries = new Query[noQueries];

    private static final String emptyInputs = "Uno o più campi vuoti!";
    private static final String resultOutput = "Risultati trovati";
    private static final String namesBadlyFormatted = "Le variabili impostate dal designer non corrispondono alla convenzione di denominazione";

    public QueryWindow() {
        init();
    }

    /**
     * Create implemented queries and initialize listeners for search buttons.
     */
    private void init() {

        for (Query.implementedQueries q : queryNames)
            queries[q.ordinal()] = new Query(q);

        for (Query.implementedQueries q : queryNames) {
            String queryName = q.name();
            JButton searchButton = (JButton) getField("search_" + queryName);
            JLabel response = (JLabel) getField("response_" + queryName);

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] params = new String[q.params];
                    for (int i = 0; i < params.length; i++) {
                        params[i] = ((JTextField) getField("param_" + queryName + "_" + i)).getText();
                        if (params[i].equals("")) {
                            response.setText(Utils.htmlStyleStr(emptyInputs,
                                    0, Utils.LabelAlignment.left, errorColor));
                            return;
                        }
                    }
                    int result = queries[q.ordinal()].execute(params);
                    if (result == 0) {
                        response.setText(resultOutput);
                        populateTable(q);
                    } else
                        response.setText(Utils.htmlStyleStr(queries[q.ordinal()].error,
                                0, Utils.LabelAlignment.left, errorColor));
                }
            });
        }

    }

    /**
     * Populate the table for the specified query.
     * Remember this has to be called after calling code.Query.execute() on the query.
     *
     * @param query for this table
     */
    private void populateTable(Query.implementedQueries query) {
        JTable table = (JTable) getField("table_" + query.name());
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        int index = query.ordinal();
        for (int j = 0; j < queries[index].columns.length; j++)
            model.addColumn(queries[index].columns[j]);
        for (Object[] row : queries[index].result)
            model.addRow(row);
    }

    /**
     * Get the java field by name for this object. This is useful in order to obtain fields generated by UI designer.
     * If each UI component is given a conventional name, it's easy to generalize listeners for search buttons, without
     * the need to write one for each query.
     *
     * @param name of the field, as it is defined in this class
     */
    private Object getField(String name) {
        Object field = null;
        try {
            field = getClass().getDeclaredField(name).get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(namesBadlyFormatted + "o non sono accessibili");
            exit(1);
        }
        return field;
    }

}
