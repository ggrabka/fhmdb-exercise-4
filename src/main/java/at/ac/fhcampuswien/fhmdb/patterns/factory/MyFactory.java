package at.ac.fhcampuswien.fhmdb.patterns.factory;

import at.ac.fhcampuswien.fhmdb.controllers.MainController;
import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;
import javafx.util.Callback;

public class MyFactory implements Callback<Class<?>, Object> {

    @Override
    public Object call(Class<?> aClass) {
        System.out.println("Factory requested: " + aClass.getName());

        try {
            Object controller = aClass.getDeclaredConstructor().newInstance();

            // Set singleton instance if needed
            if (controller instanceof MovieListController movieCtrl) {
                MovieListController.setInstance(movieCtrl);  // ← we add this static setter
            } else if (controller instanceof MainController mainCtrl) {
                MainController.setInstance(mainCtrl);
            }

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}