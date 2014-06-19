<?php

Route::group(['prefix' => 'api'], function() {
  Route::group(['prefix' => 'lang'], function() {
    Route::get('/app', ['as' => 'api.lang.app', 'uses' => 'api\lang\LangController@app']);
  });
  
  Route::group(['prefix' => 'auth'], function() {
    Route::get ('/check',    ['as' => 'api.auth.check',    'uses' => 'api\auth\AuthController@check']);
    Route::put ('/register', ['as' => 'api.auth.register', 'uses' => 'api\auth\AuthController@register']);
    Route::post('/login',    ['as' => 'api.auth.login',    'uses' => 'api\auth\AuthController@login']);
    Route::post('/logout',   ['as' => 'api.auth.logout',   'uses' => 'api\auth\AuthController@logout']);
  });
  
  Route::group(['prefix' => 'storage'], function() {
    
  });
});

Route::get('/', ['as' => 'home', 'uses' => 'HomeController@home']);
