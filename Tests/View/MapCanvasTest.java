package View;

import Enums.OSMEnums.ElementType;
import Helpers.ThemeHelper;
import KDtree.KDTree;
import KDtree.NodeGenerator;
import KDtree.Pointer;
import Model.Elements.Building;
import Model.Elements.POI;
import Model.Elements.SuperElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jakob on 01-05-2017.
 */
public class MapCanvasTest {

    MapCanvas mapCanvas;

    @Before
    public void setUp() throws Exception {
        ThemeHelper.setTheme("Default");
        mapCanvas = new MapCanvas();
    }

    @After
    public void tearDown() throws Exception {
        mapCanvas = null;
    }

    @Test
    public void setBackgroundColor() throws Exception {
        mapCanvas.setBackground(Color.BLACK);
        assertTrue(mapCanvas.getBackground().equals(Color.BLACK));

        mapCanvas.setBackground(Color.BLUE);
        assertFalse(mapCanvas.getBackground().equals(Color.RED));
    }

    @Test
    public void toggleAntiAliasing() throws Exception {
        /*
         * Test the default value of the field antiAliasing, which is set by the method
         * public void toggleAntiAliasing(boolean isAntiAliasing)
         * Before the method have been invoked.
         */
        Field field = mapCanvas.getClass().getDeclaredField("antiAliasing");
        field.setAccessible(true);
        field.get(mapCanvas);
        assertTrue(field.get(mapCanvas).equals(false));

        //Testing the value of the field antiAliasing after the toggleAntiAliasing-method have been invoked.
        mapCanvas.toggleAntiAliasing(true);
        assertTrue(field.get(mapCanvas).equals(true));

        mapCanvas.toggleAntiAliasing(false);
        assertTrue(field.get(mapCanvas).equals(false));
    }

    @Test
    public void setCoastLines() throws Exception {
        //Tests the initial value of the field coastlines before the setCoastLines method have been invoked.
        Field field = mapCanvas.getClass().getDeclaredField("coastlines");
        field.setAccessible(true);
        field.get(mapCanvas);
        assertNull(field.get(mapCanvas));

        //Building test data
        ArrayList<Point2D> listOfPoints = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i++){
            Point2D p = new Point2D.Float(i, i);
            listOfPoints.add(p);
        }
        Path2D path = new Path2D.Float();
        Point2D node = listOfPoints.get(0);
        path.moveTo(node.getX(), node.getY());
        for (int i = 1; i < listOfPoints.size(); i++) {
            node = listOfPoints.get(i);
            path.lineTo(node.getX(), node.getY());
        }
        List<Path2D> listOfPaths = new ArrayList<>();
        listOfPaths.add(path);

        //Testing setCoastLines method
        mapCanvas.setCoastLines(listOfPaths);
        assertNotNull(field.get(mapCanvas));
        assertTrue(field.get(mapCanvas).equals(listOfPaths));
    }

    @Test
    public void getCameraMaxLon() throws Exception {
        //Set field using reflection and then test getter-method.
        Field field = mapCanvas.getClass().getDeclaredField("cameraMaxLon");
        field.setAccessible(true);
        field.set(mapCanvas, 123f);
        assertTrue(field.get(mapCanvas).equals(123f));
    }

    @Test
    public void getCameraMinLon() throws Exception {
        //Set field using reflection and then test getter-method.
        Field field = mapCanvas.getClass().getDeclaredField("cameraMinLon");
        field.setAccessible(true);
        field.set(mapCanvas, 456.7f);
        assertTrue(field.get(mapCanvas).equals(456.7f));
    }

    @Test
    public void getCameraMaxLat() throws Exception {
        //Set field using reflection and then test getter-method.
        Field field = mapCanvas.getClass().getDeclaredField("cameraMaxLat");
        field.setAccessible(true);
        field.set(mapCanvas, -15f);
        assertTrue(field.get(mapCanvas).equals(-15f));
    }

    @Test
    public void getCameraMinLat() throws Exception {
        //Set field using reflection and then test getter-method.
        Field field = mapCanvas.getClass().getDeclaredField("cameraMinLat");
        field.setAccessible(true);
        field.set(mapCanvas, 0.005f);
        assertTrue(field.get(mapCanvas).equals(0.005f));
    }

    @Test
    public void setMaxLon() throws Exception {
        mapCanvas.setMaxLon(4000f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("maxLon");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(4000f));
    }

    @Test
    public void setMinLon() throws Exception {
        mapCanvas.setMinLon(-45678f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("minLon");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(-45678f));
    }

    @Test
    public void setMaxLat() throws Exception {
        mapCanvas.setMaxLat(19f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("maxLat");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(19f));
    }

    @Test
    public void setMinLat() throws Exception {
        mapCanvas.setMinLat(2.22f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("minLat");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(2.22f));
    }

    @Test
    public void setDynMaxLon() throws Exception {
        mapCanvas.setDynMaxLon(13.131313f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("dynMaxLon");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(13.131313f));
    }

    @Test
    public void setDynMinLon() throws Exception {
        mapCanvas.setDynMinLon(17f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("dynMinLon");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(17f));
    }

    @Test
    public void setDynMaxLat() throws Exception {
        mapCanvas.setDynMaxLat(123456789f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("dynMaxLat");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(123456789f));
    }

    @Test
    public void setDynMinLat() throws Exception {
        mapCanvas.setDynMinLat(8f);

        //Get field using reflection.
        Field field = mapCanvas.getClass().getDeclaredField("dynMinLat");
        field.setAccessible(true);
        assertTrue(field.get(mapCanvas).equals(8f));
    }

    @Test
    public void paintComponent() throws Exception {
        //A visual test may be best.
    }

    @Test
    public void zoom() throws Exception {
        //Get Affine transform using reflection
        Field field = mapCanvas.getClass().getDeclaredField("transform");
        field.setAccessible(true);
        AffineTransform transform = (AffineTransform)field.get(mapCanvas);

        //Testing zoom factor in transform before the zoom-method have been invoked.
        assertEquals(1.0, transform.getScaleX(), 0.000001 );

        //Testing zoom-method by looking at the scalefactor in Affine transform
        mapCanvas.zoom(170.1315);
        assertEquals(170.1315, transform.getScaleX(), 0.00001 );

        mapCanvas.zoom(10);
        assertEquals(170.1315*10, transform.getScaleX(), 0.00001 );

        mapCanvas.zoom(-2.0);
        assertEquals(1701.315 *(-2), transform.getScaleX(), 0.00001 );
    }

    @Test
    public void resetTransform() throws Exception {
        //Get Affine transform using reflection
        Field field = mapCanvas.getClass().getDeclaredField("transform");
        field.setAccessible(true);
        AffineTransform transform = (AffineTransform)field.get(mapCanvas);

        //Testing zoom factor in transform before the zoom-method have been invoked.
        assertEquals(1.0, transform.getScaleX(), 0.000001 );

        //Testing zoom-method
        mapCanvas.zoom(10);
        assertEquals(10, transform.getScaleX(), 0.00001 );

        mapCanvas.resetTransform();
        transform = (AffineTransform)field.get(mapCanvas);
        assertEquals(1, transform.getScaleX(), 0.00001 );
    }

    @Test
    public void pan() throws Exception {
        //Get Affine transform using reflection
        Field field = mapCanvas.getClass().getDeclaredField("transform");
        field.setAccessible(true);
        AffineTransform transform = (AffineTransform)field.get(mapCanvas);

        //Testing translate(pan) factor before invoking the pan-method.
        assertEquals(0, transform.getTranslateX(), 0.00001 );
        assertEquals(0, transform.getTranslateY(), 0.00001 );

        //Testing pan method
        mapCanvas.pan(13.5,17.9);
        assertEquals(13.5, transform.getTranslateX(), 0.00001);
        assertEquals(17.9, transform.getTranslateY(), 0.00001);

        mapCanvas.pan(-100,-50);
        assertEquals(-86.5, transform.getTranslateX(), 0.00001);
        assertEquals(-32.1, transform.getTranslateY(), 0.00001);
    }

    @Test
    public void toModelCoords() throws Exception {
        //Panning values positive, model coordinates go from positive to negative
        mapCanvas.pan(10.2,10.2);
        Point2D point = mapCanvas.toModelCoords(new Point2D.Float(10,10));
        assertEquals(-0.2, point.getX(), 0.00001);
        assertEquals(-0.2, point.getY(), 0.00001);
        //Panning values negative, model coordinates go from negative to positive, multiple pans before calculating model coordinates.
        mapCanvas.pan(-0.4,-0.4);
        point = mapCanvas.toModelCoords(new Point2D.Float(10,10));
        assertEquals(0.2, point.getX(), 0.00001);
        assertEquals(0.2, point.getY(), 0.00001);
    }

    @Test
    public void setElements() throws Exception {
        //Testing the elements field status before setElement method have been invoked.
        Field field = mapCanvas.getClass().getDeclaredField("elements");
        field.setAccessible(true);
        assertNull(field.get(mapCanvas));

        //Setting up an EnumMap<ElementType, KDTree> and invoke the setElements method
        EnumMap<ElementType, KDTree> elements = new EnumMap<>(ElementType.class);
        for (ElementType type : ElementType.values()) {
            elements.put(type, new KDTree());
        }
        mapCanvas.setElements(elements);
        assertNotNull(field.get(mapCanvas));

        //Adding an element to one of the KDTrees in elements to check if that element is a part of elements when set.
        NodeGenerator nodeGenerator = new NodeGenerator(10, 2);
        for (int i = 0; i < 10; i++) {
            nodeGenerator.addPoint(new Point2D.Float(i, i * 2));
        }
        nodeGenerator.initialise();
        for (ElementType type : ElementType.values()) {
            nodeGenerator.setupTree(elements.get(type));
        }
        Building building = new Building(null);
        Pointer p = new Pointer(11,11, building);
        elements.get(ElementType.BUILDING).putPointer(p);
        mapCanvas.setElements(elements);
        elements.get(ElementType.BUILDING).getManySections(0,0,20,20).size();
        assertEquals(1, elements.get(ElementType.BUILDING).getManySections(0,0,20,20).size());
        assertTrue(elements.get(ElementType.BUILDING).getManySections(0,0,20,20).contains(building));

        //Testing when adding null
        mapCanvas.setElements(null);
        assertNull(field.get(mapCanvas));
    }

    @Test
    public void setCurrentSection() throws Exception {
        //Testing the HashSet<Element> currentSection field before it is set with the setCurrentSection method.
        Field field = mapCanvas.getClass().getDeclaredField("currentSection");
        field.setAccessible(true);
        assertNull(field.get(mapCanvas));

        //Setting up data for the test
        HashSet<SuperElement> hashSet = new HashSet<>();
        for(int i = 0 ; i < 10 ; i++){
            Building building = new Building(null);
            hashSet.add(building);
        }

        mapCanvas.setCurrentSection(hashSet);
        assertSame(hashSet, field.get(mapCanvas));

        //Testing when adding null
        mapCanvas.setCurrentSection(null);
        assertNull(field.get(mapCanvas));
    }

    @Test
    public void setCurrentPoint() throws Exception {
        //Testing the field Point2D currentPoint before the setCurrentPoint have been invoked.
        Field field = mapCanvas.getClass().getDeclaredField("currentPoint");
        field.setAccessible(true);
        assertNull(field.get(mapCanvas));

        //Testing the method with a valid Point2d.Float, positive coordinates
        Point2D point = new Point2D.Float(34f,34f);
        mapCanvas.setCurrentPoint(point);
        assertSame(point, field.get(mapCanvas));

        //Testing with a valid Point2d.Double, negative coordinates
        point = new Point2D.Double(-10.2, -10.2);
        mapCanvas.setCurrentPoint(point);
        assertSame(point, field.get(mapCanvas));

        //Testing the method with a null value
        mapCanvas.setCurrentPoint(null);
        assertNull(field.get(mapCanvas));
    }

    @Test
    public void setLocationMarker() throws Exception {
        //Testing the field Point2D.Float locationMarker before the setLocationMarker have been invoked.
        Field field = mapCanvas.getClass().getDeclaredField("locationMarker");
        field.setAccessible(true);
        assertNull(field.get(mapCanvas));

        //Testing the method with a valid Point2d.Float, positive coordinates
        Point2D.Float point = new Point2D.Float(11.1f,11.2f);
        mapCanvas.setLocationMarker(point);
        assertSame(point, field.get(mapCanvas));

        //Testing the method with a valid Point2d.Float, negative coordinates
        point = new Point2D.Float(-11.1f,-11.2f);
        mapCanvas.setLocationMarker(point);
        assertSame(point, field.get(mapCanvas));

        //Testing the method with a null value
        mapCanvas.setLocationMarker(null);
        assertNull(field.get(mapCanvas));
    }

    @Test
    public void panToPoint() throws Exception {
        //Get Affine transform using reflection
        Field field = mapCanvas.getClass().getDeclaredField("transform");
        field.setAccessible(true);
        AffineTransform transform = (AffineTransform)field.get(mapCanvas);

        //Testing translate(pan) factor before invoking the pan-method.
        assertEquals(0, transform.getTranslateX(), 0.00001 );
        assertEquals(0, transform.getTranslateY(), 0.00001 );

        //Testing panning with positive point coordinates
        Point2D point = new Point2D.Float(10f,10f);
        mapCanvas.panToPoint(point);
        assertEquals(-10, transform.getTranslateX(), 0.00001);
        assertEquals(-10, transform.getTranslateY(), 0.00001);

        //Testing panning with negative point coordinates
        point = new Point2D.Float(-100f,-100f);
        mapCanvas.panToPoint(point);
        assertEquals(100, transform.getTranslateX(), 0.00001);
        assertEquals(100, transform.getTranslateY(), 0.00001);
    }

    @Test
    public void setPOIs() throws Exception {
        //Testing the field ArrayList<POI> poiList before the setPOI method have been invoked.
        Field field = mapCanvas.getClass().getDeclaredField("poiList");
        field.setAccessible(true);
        assertNotNull(field.get(mapCanvas));

        ArrayList<POI> list = (ArrayList<POI>)field.get(mapCanvas);
        assertEquals(0, list.size());

        //Setting up data for the test
        ArrayList<POI> newList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            POI poi = new POI(10,10, "Some description " + i);
            newList.add(poi);
        }

        //Testing if not null
        mapCanvas.setPOIs(newList);
        assertNotNull(field.get(mapCanvas));

        //Testing if he size of the list is right
        list = (ArrayList<POI>)field.get(mapCanvas);
        assertEquals(10, list.size());

        //Testing if it is indeed the rigt list that have been set
        assertSame(newList, field.get(mapCanvas));
    }
}