<?php namespace api;

use Auth;
use Controller;
use Input;
use Response;
use Validator;

class AuthController extends Controller {
  public function login() {
    $validator = Validator::make(Input::all(), [
      'email'    => ['required', 'email', 'exists:users,email'],
      'password' => ['required', 'min:8', 'max:256']
    ]);
    
    if($validator->passes() && Auth::attempt(['email' => Input::get('email'), 'password' => Input::get('password')])) {
      return Response::json(null, 204);
    }
    
    return Response::json($validator->messages(), 409);
  }
}