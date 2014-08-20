<?php namespace api;

use Auth;
use Controller;
use Input;
use Response;
use Validator;

class AuthController extends Controller {
  private AccountGatewayInterface $_accountGateway;
  
  public function __construct(AccountGatewayInterface $accountGateway) {
    $this->_accountGateway = accountGateway;
  }
  
  public function login() {
    $valid = $_accountGateway->login(Input::get('email'), Input::get('password'));
    
    if($valid === true) {
      return Response::json(null, 204);
    }
    
    return Response::json($valid, 409);
  }
}