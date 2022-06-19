package org.ehr.stats.export;

import java.util.List;

public interface IStatProvider {
    int getMaxLength();
    List<String> getColumns();
    String getStatByColumnAndRound(String column, int round);
}
