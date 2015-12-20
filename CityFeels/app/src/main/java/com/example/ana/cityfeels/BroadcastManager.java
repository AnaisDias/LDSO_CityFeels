package com.example.ana.cityfeels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastManager {

    public static final String LOCATION_NEW = "com.example.ana.cityfeels.BroadcastManager.LOCATION_NEW";
    public static final String INSTRUCTIONS_NEW = "com.example.ana.cityfeels.BroadcastManager.INSTRUCTIONS_NEW";
    public static final String PONTO_INTERESSE_NEW = "com.example.ana.cityfeels.BroadcastManager.PONTO_INTERESSE_NEW";
    public static final String CONNECTIVITY_ACTIVE = "com.example.ana.cityfeels.BroadcastManager.CONNECTIVITY_ACTIVE";
    public static final String CONNECTIVITY_INACTIVE = "com.example.ana.cityfeels.BroadcastManager.CONNECTIVITY_INACTIVE";

    private LocalBroadcastManager broadcaster;

    public BroadcastManager(Context context) {
        this.broadcaster = LocalBroadcastManager.getInstance(context);
    }

    public void broadcastNewLocation() {
        broadcaster.sendBroadcast(new Intent(LOCATION_NEW));
    }

    public void broadcastNewInstructions() {
        broadcaster.sendBroadcast(new Intent(INSTRUCTIONS_NEW));
    }

    public void broadcastNewPontoInteresse() {
        broadcaster.sendBroadcast(new Intent(PONTO_INTERESSE_NEW));
    }

    public void broadcastConnectivityActive() {
        broadcaster.sendBroadcast(new Intent(CONNECTIVITY_ACTIVE));
    }

    public void broadcastConnectivityInactive() {
        broadcaster.sendBroadcast(new Intent(CONNECTIVITY_INACTIVE));
    }

    public void registerOnNewLocation(Context context, BroadcastReceiver receiver) {
        this.register(context, receiver, new IntentFilter(LOCATION_NEW));
    }

    public void registerOnNewInstructions(Context context, BroadcastReceiver receiver) {
        this.register(context, receiver, new IntentFilter(INSTRUCTIONS_NEW));
    }

    public void registerOnNewPontoInteresse(Context context, BroadcastReceiver receiver) {
        this.register(context, receiver, new IntentFilter(PONTO_INTERESSE_NEW));
    }

    public void registerOnConnectivityActive(Context context, BroadcastReceiver receiver) {
        this.register(context, receiver, new IntentFilter(CONNECTIVITY_ACTIVE));
    }

    public void registerOnConnectivityInactive(Context context, BroadcastReceiver receiver) {
        this.register(context, receiver, new IntentFilter(CONNECTIVITY_INACTIVE));
    }

    private void register(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

}
