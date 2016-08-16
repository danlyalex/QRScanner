package com.example.qr_codescan.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class BroadcastProvider implements Observable.OnSubscribe<Intent> {
    private BroadcastReceiver broadcastReceiver;
    private final BroadcastRegistrarStrategy broadcastRegistrarStrategy;

    BroadcastProvider(BroadcastRegistrarStrategy broadcastRegistrarStrategy) {
        this.broadcastRegistrarStrategy = broadcastRegistrarStrategy;
    }

    @Override
    public void call(final Subscriber<? super Intent> subscriber) {

        broadcastReceiver = createBroadcastReceiver(subscriber);
        broadcastRegistrarStrategy.registerBroadcastReceiver(broadcastReceiver);

        final Subscription subscription = createSubscription();
        subscriber.add(subscription);
    }

    private Subscription createSubscription() {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                broadcastRegistrarStrategy.unregisterBroadcastReceiver(broadcastReceiver);
            }
        });
    }

    BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }


    private BroadcastReceiver createBroadcastReceiver(final Subscriber<? super Intent> subscriber) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(intent);
                }
            }
        };
    }
}
