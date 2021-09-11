package com.lefc.jambly;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.times;

class ErrorParserTest {

    @Test
    @Ignore
    void print_error() throws Exception{
        ErrorParser errorParser = new ErrorParser();
        PrintText printText = new PrintText();


        errorParser.print_error(asList(printText), 1);

/*        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(file, times(2)).writeFileErr(intCaptor.capture());
        assertThat(intCaptor.getAllValues(), hasItems(456));*/
    }
}