package cag.metro.bloq.utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableGeneratorByLine
{

    private int PADDING_SIZE = 2;
    private String NEW_LINE = "\n";
    private String TABLE_JOINT_SYMBOL = "+";
    private String TABLE_V_SPLIT_SYMBOL = "|";
    private String TABLE_H_SPLIT_SYMBOL = "-";

    public List<String> generateTable(List<String> headersList, List<List<String>> rowsList,
            int... overRiddenHeaderHeight)
    {
        List<String> tabla = new ArrayList<String>(0);

        int rowHeight = overRiddenHeaderHeight.length > 0 ? overRiddenHeaderHeight[0] : 1;

        Map<Integer, Integer> columnMaxWidthMapping = getMaximumWidhtofTable(headersList, rowsList);

        tabla.add(NEW_LINE);
        
        createRowLine(tabla, headersList.size(), columnMaxWidthMapping);
        
        StringBuilder cabeceraBuilder = new StringBuilder();
        
        for (int headerIndex = 0; headerIndex < headersList.size(); headerIndex++)
        {
            fillCell(cabeceraBuilder, headersList.get(headerIndex), headerIndex, columnMaxWidthMapping);
        }
        
        tabla.add(cabeceraBuilder.toString());
        
        createRowLine(tabla, headersList.size(), columnMaxWidthMapping);

        for (List<String> row : rowsList)
        {
            StringBuilder valoresBuilder = new StringBuilder();

            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++)
            {
                fillCell(valoresBuilder, row.get(cellIndex), cellIndex, columnMaxWidthMapping);
            }
            tabla.add(valoresBuilder.toString());
        }
        
        createRowLine(tabla, headersList.size(), columnMaxWidthMapping);

        return tabla;
    }

    private void fillSpace(StringBuilder stringBuilder, int length)
    {
        for (int i = 0; i < length; i++)
        {
            stringBuilder.append(" ");
        }
    }

    private void createRowLine(List<String> tabla, int headersListSize,
            Map<Integer, Integer> columnMaxWidthMapping)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < headersListSize; i++)
        {
            if (i == 0)
            {
                stringBuilder.append(TABLE_JOINT_SYMBOL);
            }

            for (int j = 0; j < columnMaxWidthMapping.get(i) + PADDING_SIZE * 2; j++)
            {
                stringBuilder.append(TABLE_H_SPLIT_SYMBOL);
            }
            stringBuilder.append(TABLE_JOINT_SYMBOL);
        }
        tabla.add(stringBuilder.toString());
    }

    private Map<Integer, Integer> getMaximumWidhtofTable(List<String> headersList, List<List<String>> rowsList)
    {
        Map<Integer, Integer> columnMaxWidthMapping = new HashMap<>();

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++)
        {
            columnMaxWidthMapping.put(columnIndex, 0);
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++)
        {

            if (headersList.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex))
            {
                columnMaxWidthMapping.put(columnIndex, headersList.get(columnIndex).length());
            }
        }

        for (List<String> row : rowsList)
        {

            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++)
            {

                if (row.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex))
                {
                    columnMaxWidthMapping.put(columnIndex, row.get(columnIndex).length());
                }
            }
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++)
        {

            if (columnMaxWidthMapping.get(columnIndex) % 2 != 0)
            {
                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
            }
        }

        return columnMaxWidthMapping;
    }

    private int getOptimumCellPadding(int cellIndex, int datalength, Map<Integer, Integer> columnMaxWidthMapping,
            int cellPaddingSize)
    {
        if (datalength % 2 != 0)
        {
            datalength++;
        }

        if (datalength < columnMaxWidthMapping.get(cellIndex))
        {
            cellPaddingSize = cellPaddingSize + (columnMaxWidthMapping.get(cellIndex) - datalength) / 2;
        }

        return cellPaddingSize;
    }

    private void fillCell(StringBuilder stringBuilder, String cell, int cellIndex,
            Map<Integer, Integer> columnMaxWidthMapping)
    {
        int cellPaddingSize = getOptimumCellPadding(cellIndex, cell.length(), columnMaxWidthMapping, PADDING_SIZE);

        if (cellIndex == 0)
        {
            stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
        }

        fillSpace(stringBuilder, cellPaddingSize);
        stringBuilder.append(cell);
        if (cell.length() % 2 != 0)
        {
            stringBuilder.append(" ");
        }

        fillSpace(stringBuilder, cellPaddingSize);

        stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
    }

     public static void main(String[] args) {
        TableGeneratorByLine tableGenerator = new TableGeneratorByLine();

        List<String> headersList = new ArrayList<>();
        headersList.add("Tramos SMI");
        headersList.add("% No embargable");
        headersList.add("% embargable");
        headersList.add("No embargable");
        headersList.add("Embargable");

        List<List<String>> rowsList = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            List<String> row = new ArrayList<>();
            row.add("XXXX,XX");
            row.add("XXXX,XX");
            row.add("XXXX,XX");
            row.add("XXXX,XX");
            row.add("XXXX,XX");

            rowsList.add(row);
        }
        
        tableGenerator.generateTable(headersList, rowsList).forEach(line -> {
            System.out.println(line);
        });
    }
}
