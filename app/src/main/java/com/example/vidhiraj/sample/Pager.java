//package com.example.vidhiraj.sample;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//
///**
// * Created by Lenovo on 11/11/2016.
// */
//
//public class Pager extends FragmentStatePagerAdapter {
//
//    //integer to count number of tabs
//    int tabCount;
//
//    //Constructor to the class
//    public Pager(FragmentManager fm, int tabCount) {
//        super(fm);
//        //Initializing tab count
//        this.tabCount= tabCount;
//    }
//
//    //Overriding method getItem
//    @Override
//    public Fragment getItem(int position) {
//        //Returning the current tabs
//        switch (position) {
//            case 0:
//                CreateFragment tab1 = new CreateFragment();
//                return tab1;
//            case 1:
//                DailyCatalogFragment tab2 = new DailyCatalogFragment();
//                return tab2;
//            case 2:
//                StudentFragment tab3 = new StudentFragment();
//                return tab3;
//            default:
//                return null;
//        }
//    }
//
//    //Overriden method getCount to get the number of tabs
//    @Override
//    public int getCount() {
//        return tabCount;
//    }
//}