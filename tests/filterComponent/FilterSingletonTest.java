package filterComponent;


import authentification.loginProcess.StrategyContext;
import authentification.loginProcess.UserStrategy;
import javafx.collections.ObservableList;
import models.Events;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterSingletonTest {

    private FilterSingleton filter;

    @Before
    public void setUp() throws Exception {
        StrategyContext strategyContext;
        String username = "Gleaves@hs-ulm.de";
        String password = "user85";
        strategyContext = new StrategyContext(new UserStrategy());
        strategyContext.executeStrategy(username, password);
        filter = FilterSingleton.getInstance();
    }


    @Test
    public void setCityValue() {
        filter.setCityValue("Ulm");
        assertEquals("Ulm", filter.getCityValue());
    }

    @Test
    public void setRadiusValue() {
        filter.setRadiusValue(20);
        assertEquals(20, filter.getRadiusValue());
    }

    @Test
    public void setMinPersValue() {
        filter.setMinPersValue(20);
        assertEquals(20, filter.getMinPersValue());
    }

    @Test
    public void setPriceValue() {
        filter.setPriceValue(58.56);
        assertEquals(58.56, filter.getPriceValue(),0.01);
    }
    @Test
    public void setSortSelected() {
        filter.setSortSelected(4);
        assertEquals(4, filter.getSortSelected());
    }

    @Test
    public void setRadiusSelected() {
        filter.setRadiusSelected(7);
        assertEquals(7, filter.getRadiusSelected());
    }

    @Test
    public void setMinPersSelected() {
        filter.setMinPersSelected(3);
        assertEquals(3, filter.getMinPersSelected());
    }

    @Test
    public void getRadiusSelected() {
        filter.setRadiusValue(39);
        assertEquals(39, filter.getRadiusValue());
    }

    @Test
    public void getMinPersSelected() {
        filter.setMinPersSelected(4);
        assertEquals(4, filter.getMinPersSelected());
    }

    @Test
    public void getSortSelected() {
        filter.setSortSelected(1);
        assertEquals(1, filter.getSortSelected());
    }

    @Test
    public void getRadiusValue() {
        filter.setRadiusValue(97);
        assertEquals(97, filter.getRadiusValue());
    }

    @Test
    public void getBackup() {
        ObservableList<Events> list = filter.getBackup();
        assertTrue(list.size() > 0);
    }

    @Test
    public void getInstance() {
        FilterSingleton filterSingleton = FilterSingleton.getInstance();
        assertNotNull(filterSingleton);
    }

    @Test
    public void getCityValue() {
        filter.setCityValue("Amsterdam");
        assertEquals( "Amsterdam", filter.getCityValue());
    }

    @Test
    public void getPriceValue() {
        filter.setPriceValue(43.51);
        assertEquals(43.51, filter.getPriceValue(),0.01);
    }

    @Test
    public void resetFilter() {
        filter.resetFilter();
        assertNull(filter.getCityValue());
        assertEquals(100.00, filter.getPriceValue(), 0.01);
        assertEquals(-1,filter.getMinPersSelected());
        assertEquals(-1,filter.getRadiusSelected());
    }

    @Test
    public void filterItems() {
        filter.resetFilter();
        filter.filterItems();
    }

}
