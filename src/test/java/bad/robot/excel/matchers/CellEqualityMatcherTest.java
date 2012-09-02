/*
 * Copyright (c) 2012, bad robot (london) ltd.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package bad.robot.excel.matchers;

import org.apache.poi.ss.usermodel.Row;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static bad.robot.excel.WorkbookResource.firstRowOf;
import static bad.robot.excel.WorkbookResource.secondRowOf;
import static bad.robot.excel.matchers.CellEqualityMatcher.hasSameCellsAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CellEqualityMatcherTest {

    private StringDescription description = new StringDescription();

    private Row firstRow;
    private Row secondRow;
    private Row firstRowWithAlternateValues;
    private Row secondRowWithAlternateValues;

    @Before
    public void loadWorkbookAndSheets() throws IOException {
        firstRow = firstRowOf("rowWithThreeCells.xls");
        secondRow = secondRowOf("rowWithThreeCells.xls");
        firstRowWithAlternateValues = firstRowOf("rowWithThreeCellsAlternativeValues.xls");
        secondRowWithAlternateValues = secondRowOf("rowWithThreeCellsAlternativeValues.xls");
    }

    @Test
    public void exampleUsage() {
        assertThat(firstRow, hasSameCellsAs(firstRow));
        assertThat(firstRowWithAlternateValues, not(hasSameCellsAs(firstRow)));
        assertThat(secondRowWithAlternateValues, not(hasSameCellsAs(secondRow)));
    }

    @Test
    public void matches() {
        assertThat(hasSameCellsAs(firstRow).matches(firstRow), is(true));
    }

    @Test
    public void doesNotMatch() {
        assertThat(hasSameCellsAs(firstRowWithAlternateValues).matches(firstRow), is(false));
    }

    @Test
    public void description() {
        hasSameCellsAs(firstRow).describeTo(description);
        assertThat(description.toString(), is("equality of all cells of row <1>"));
    }

    @Test
    public void mismatch() {
        hasSameCellsAs(firstRow).matchesSafely(firstRowWithAlternateValues, description);
        assertThat(description.toString(), is("cell at \"B1\" contained <3.14D> expected <\"C2, R1\">"));
    }

    @Test
    public void mismatchOnMissingCell() {
        hasSameCellsAs(secondRow).matchesSafely(secondRowWithAlternateValues, description);
        assertThat(description.toString(), is("cell at \"B2\" contained <nothing> expected <\"C2, R2\">"));
    }
}
