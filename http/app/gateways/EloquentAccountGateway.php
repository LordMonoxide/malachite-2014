<?php

class EloquentAccountGateway implements AccountGatewayInterface {
  public function validateLogin($email, $password) {
    $validator = Validator::make(Input::all(), [
      'email'    => ['required', 'email', 'exists:users,email'],
      'password' => ['required', 'min:8', 'max:256']
    ]);
    
    return $validator->passes() ? true : $validator->messages();
  }
  
  public function login($email, $password) {
    $validator = validateLogin($email, $password);
    
    if($validator === true && Auth::attempt(['email' => $email, 'password' => $password])) {
      return true;
    }
    
    return $validator;
  }
}