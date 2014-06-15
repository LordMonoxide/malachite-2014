<?php

Route::group(['prefix' => 'api'], function() {
  Route::group(['prefix' => 'lang'], function() {
    Route::get('/app', ['as' => 'api.lang.app', 'uses' => 'api\lang\LangController@app']);
  });
});

Route::get('/', ['as' => 'home', 'uses' => 'HomeController@home']);
