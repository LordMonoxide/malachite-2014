<?php namespace api\auth;

use Auth;
use Controller;
use Input;
use Response;
use Validator;

class AuthController extends Controller {
  public function __construct() {
    $protected = ['check', 'logout'];
    $this->beforeFilter('auth.api',  ['only'   => $protected]);
    $this->beforeFilter('guest.api', ['except' => $protected]);
  }
  
  public function check() {
    return Response::json(null, 204);
  }
  
  public function register() {
    $validator = Validator::make(Input::all(), [
      'email'      => ['required', 'email', 'unique:users,email'],
      'password'   => ['required', 'min:8', 'max:256', 'confirmed'],
      'name_first' => ['required', 'min:2', 'max:30'],
      'name_last'  => ['min:2', 'max:30']
    ]);
    
    if($validator->passes()) {
      $user = new User;
      $user->email      = Input::get('email');
      $user->password   = Hash::make(Input::get('password'));
      $user->name_first = Input::get('name_first');
      $user->name_last  = Input::get('name_last');
      $user->save();
      
      $remember = Input::has('remember');
      
      Auth::login($user, $remember);
      
      return Response::json(['id' => Auth::user()->id], 200);
    } else {
      return Response::json($validator->messages(), 409);
    }
  }
  
  public function login() {
    $validator = Validator::make(Input::all(), [
      'email'    => ['required', 'email', 'exists:users,email'],
      'password' => ['required', 'min:8', 'max:256']
    ]);
    
    if($validator->passes()) {
      $remember = Input::has('remember');
      
      if(!Auth::attempt(Input::only(['email', 'password']), $remember)) {
        return Response::json(['password' => ['Invalid password']], 409);
      }
      
      return Response::json(['id' => Auth::user()->id], 200);
    } else {
      return Response::json($validator->messages(), 409);
    }
  }
  
  public function logout() {
    Auth::logout();
    return Response::json(null, 204);
  }
}