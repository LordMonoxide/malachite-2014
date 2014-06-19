<?php

App::before(function($request) {
  
});

App::after(function($request, $response) {
  
});

Route::filter('auth', function() {
  if(Auth::guest()) {
    if(Request::ajax()) {
      return Response::make('Unauthorized', 401);
    } else {
      return Redirect::guest('login');
    }
  }
});

Route::filter('auth.basic', function() {
  return Auth::basic();
});

Route::filter('auth.api', function() {
  if(Auth::guest()) {
    return Response::json([
      'error' => 'not-logged-in',
      'show'  => 'login'
    ], 401);
  }
});

Route::filter('guest', function() {
  if(Auth::check()) {
    return Redirect::to('/');
  }
});

Route::filter('guest.api', function() {
  if(Auth::check()) {
    return Response::json([
      'error' => 'already-logged-in',
      'show'  => 'home'
    ], 409);
  }
});

Route::filter('csrf', function() {
  if(Session::token() != Input::get('_token')) {
    throw new Illuminate\Session\TokenMismatchException;
  }
});
