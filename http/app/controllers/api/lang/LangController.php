<?php namespace api\lang;

use Controller;
use Lang;
use Response;

class LangController extends Controller {
  public function app() {
    return Response::json(Lang::get('api.app'), 200);
  }
}