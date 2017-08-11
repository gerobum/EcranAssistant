/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumerations;

/**
 *
 * @author maillot
 */

public enum Day {
        CHAQUE_JOUR("Tous les jours", "*"), 
        LUNDI("Lundi", "0"), 
        MARDI("Mardi", "1"), 
        MERCREDI("Mercredi", "2"), 
        JEUDI("Jeudi", "3"), 
        VENDREDI("Vendredi", "4"), 
        SAMEDI("Samedi", "5"), 
        DIMANCHE("Dimanche", "6");
        
        private final String day;
        private final String abv;

        private Day(String day, String abv) {
            this.day = day;
            this.abv = abv;
        }

        @Override
        public String toString() {
            return day;
        }
        
        public String abv() {
            return abv;
        }
        
    }
    
