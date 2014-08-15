<?php

Route::group(['prefix' => 'api', 'namespace' => 'api'], function() {
  Route::put('/login', ['as' => 'api.login', 'uses' => 'AuthController@login']);
});