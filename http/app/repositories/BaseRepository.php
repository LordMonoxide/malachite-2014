<?php

abstract class BaseRepository {
  protected $model;
  
  public function all() {
    return $this->model->all();
  }
  
  public function get($id) {
    return $this->model->where('id', $id)->take(1)->first();
  }
  
  public function delete($id) {
    $this->model->destroy($id);
  }
  
  public function create(array $data) {
    foreach($data as $key => $val) {
      $model->$key = $val;
    }
    
    $model->save();
  }
  
  public function update(array $data) {
    $this->model = $this->model->where('id', $data->id);
    
    foreach(array_except($data, 'id') as $key => $val) {
      $model->$key = $val;
    }
    
    $model->save();
  }
}