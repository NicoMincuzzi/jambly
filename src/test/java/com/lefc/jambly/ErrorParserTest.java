package com.lefc.jambly;

import com.lefc.jambly.repository.SemanticErrorRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

class ErrorParserTest {

    @Test
    @Ignore
    void print_error() throws Exception{
        SemanticErrorRepository semanticError = mock(SemanticErrorRepository.class);

        ErrorParser errorParser = new ErrorParser(semanticError);
        PrintText printText = new PrintText();


        errorParser.print(asList(printText), 1);

/*        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(file, times(2)).writeFileErr(intCaptor.capture());
        assertThat(intCaptor.getAllValues(), hasItems(456));*/
    }
}