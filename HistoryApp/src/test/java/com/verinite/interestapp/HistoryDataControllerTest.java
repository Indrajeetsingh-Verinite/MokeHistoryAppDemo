package com.verinite.interestapp;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.verinite.interestapp.dto.request.RequestAttribute;
import com.verinite.interestapp.dto.request.RequestBalance;
import com.verinite.interestapp.dto.request.RequestCategories;
import com.verinite.interestapp.dto.request.RequestHistoryData;
import com.verinite.interestapp.dto.response.ResponseHistoryData;
import com.verinite.interestapp.exception.InterestAppException;
import com.verinite.interestapp.exception.ErrorCodes;

public class HistoryDataControllerTest extends HistorymoduleApplicationTest {

    RequestHistoryData requestHistoryData;

    @Before
    public void setup() {
        super.setup();
        requestHistoryData = createData();
    }

    RequestHistoryData createData() {

        List<RequestBalance> balanceList = new ArrayList<>();
        balanceList.add(new RequestBalance(5000f, "Conditional", 10.5f, 500f));
        List<RequestCategories> requestCategoriesList = new ArrayList<>();

        requestCategoriesList.add(new RequestCategories("2021-03-30 23:23:23", balanceList));

        RequestHistoryData requestHistoryData = new RequestHistoryData("1.0", "2021-03-25 23:23:23",
                "2021-03-30 23:23:23", "Hdfc", new RequestAttribute("LI0013", "ST0013", requestCategoriesList));

        return requestHistoryData;
    }

    @Test
    public void create() throws IOException {
        ResponseEntity<ResponseHistoryData> response = historyDataController.historyCreation(requestHistoryData);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(requestHistoryData.getIfi(), response.getBody().getIfi());
        assertEquals(requestHistoryData.getVersion(), response.getBody().getVersion());
    }

    @Test
    public void createThrowsIdNullException() {
        requestHistoryData.setValidfrom(null);
        requestHistoryData.setValidtill(null);

        InterestAppException thrown = assertThrows("id invalid", InterestAppException.class,
                () -> historyDataController.historyCreation(requestHistoryData));
        assertNotNull(thrown);

    }

    @Test
    public void delete() throws IOException {
        ResponseEntity<ResponseHistoryData> response = historyDataController.historyCreation(requestHistoryData);
        ResponseEntity<Object> responseDelete = historyDataController.histroyDeletionById(response.getBody().getId());
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());

    }

    @Test
    public void update() throws IOException{
        ResponseEntity<ResponseHistoryData> response = historyDataController.historyCreation(requestHistoryData);
        RequestHistoryData requestHistorydata = new RequestHistoryData();
        requestHistorydata.setIfi("new");
        requestHistorydata.setValidfrom("2021-03-02 05:06:08");
        requestHistorydata.setValidtill("2021-03-08 08:09:05");

        ResponseEntity<ResponseHistoryData> responseUpdate = historyDataController.histroyUpdation(requestHistorydata,
                response.getBody().getId());

        assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());
        assertNotNull(responseUpdate);
        assertEquals(requestHistorydata.getIfi(), responseUpdate.getBody().getIfi());
    }

    @Test
    public void get() throws IOException {
        ResponseEntity<ResponseHistoryData> response = historyDataController.historyCreation(requestHistoryData);
        ResponseEntity<ResponseHistoryData> responseGet = historyDataController.getHistoryById(response.getBody().getId());

        assertEquals(HttpStatus.OK, responseGet.getStatusCode());
        assertNotNull(responseGet);
        assertEquals(requestHistoryData.getIfi(), responseGet.getBody().getIfi());
        assertEquals(requestHistoryData.getVersion(), responseGet.getBody().getVersion());
    }

    @Test
    public void getAllRecordesInAsc() throws IOException {
        Integer pageNo = 0;
        Integer pageSize = 2;
        String direction = "asc";
        historyDataController.historyCreation(requestHistoryData);
        ResponseEntity<Page<ResponseHistoryData>> responseGetAll = historyDataController.getHistory(pageNo, pageSize, direction);
        assertEquals(HttpStatus.OK, responseGetAll.getStatusCode());
        assertNotNull(responseGetAll);
        assertThat(responseGetAll.getBody().getNumberOfElements()).isBetween(0, pageSize);

    }

    @Test
    public void invalidId() {

        InterestAppException thrown = assertThrows(ErrorCodes.ID_NOT_PRESENT, InterestAppException.class,
                () -> historyDataController.getHistoryById(1));
        assertNotNull(thrown);
        
    }

    @Test
    public void getAllRecordesInDesc() throws IOException {
        Integer pageNo = 0;
        Integer pageSize = 2;
        String direction = "desc";
        historyDataController.historyCreation(requestHistoryData);
        ResponseEntity<Page<ResponseHistoryData>> responseGetAll = historyDataController.getHistory(pageNo, pageSize,
                direction);
        assertEquals(HttpStatus.OK, responseGetAll.getStatusCode());
        assertNotNull(responseGetAll);
        assertThat(responseGetAll.getBody().getNumberOfElements()).isBetween(0, pageSize);

    }

    @Test
    public void deleteIfIdNotPresent() {
        InterestAppException thrown = assertThrows(ErrorCodes.ID_NOT_PRESENT, InterestAppException.class,
                () -> historyDataController.histroyDeletionById(1));
        assertNotNull(thrown);

    }

    @Test
    public void updateWhenIdNotPresent() {
        RequestHistoryData requestHistorydata = new RequestHistoryData();
        requestHistorydata.setIfi("new");
        requestHistorydata.setValidfrom("2021-03-02 05:06:08");
        requestHistorydata.setValidtill("2021-03-08 08:09:05");

        InterestAppException thrown = assertThrows(ErrorCodes.ID_NOT_PRESENT, InterestAppException.class,
                                            () -> historyDataController.histroyUpdation(requestHistorydata,1));
        assertNotNull(thrown);
    }

    @Test
    public void getAllRecordesInInvalidDirection() throws IOException {
        Integer pageNo = 0;
        Integer pageSize = 2;
        String direction = "abc";
        historyDataController.historyCreation(requestHistoryData);

        InterestAppException thrown = assertThrows(ErrorCodes.PAGINATION_DIRECTION_NOT_VALID, InterestAppException.class,
            () -> historyDataController.getHistory(pageNo, pageSize,direction));
        assertNotNull(thrown);

    

    }

    @Test
    public void updateWhenValidSmallerThenTill() {
        RequestHistoryData requestHistorydata = new RequestHistoryData();
        requestHistorydata.setIfi("new");
        requestHistorydata.setValidfrom("2021-03-08 05:06:08");
        requestHistorydata.setValidtill("2021-03-02 08:09:05");

        InterestAppException thrown = assertThrows(ErrorCodes.DATE_CONDITION_FAILED, InterestAppException.class,
                () -> historyDataController.histroyUpdation(requestHistorydata,1));
        assertNotNull(thrown);
    }

    @Test
    public void createWhenValidSmallerThenTill() {
        RequestHistoryData requestHistorydata = new RequestHistoryData();
        requestHistorydata.setIfi("new");
        requestHistorydata.setValidfrom("2021-03-08 05:06:08");
        requestHistorydata.setValidtill("2021-03-02 08:09:05");

        InterestAppException thrown = assertThrows(ErrorCodes.DATE_CONDITION_FAILED, InterestAppException.class,
                () -> historyDataController.histroyUpdation(requestHistorydata,1));
        assertNotNull(thrown);
    }


}
