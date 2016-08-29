<?php
$content = new GetSnippetDynamicContent();
$content->id = 1211;
$content->dynamicContentId = "RVMtZWRpdF90ZXh0XzE=";
print_r($content->getData());

class GetSnippetDynamicContent{
	private $host = "CHANGE ME";
	private $clientId = "CHANGE ME";
	private $clientSecret = "CHANGE ME";
	public $id;//id of  to retrieve content from, required
	public $dynamicContentId; //id of dynamic content section, required
	
	public function getData(){
		$url = $this->host . "/rest/asset/v1/snippet/" . $this->id . "/dynamicContent/" . $this->dynamicContentId . ".json?access_token=" . $this->getToken();
		if (isset($this->status)){
			$url .= "&status=" . $this->status;
		}
		$ch = curl_init($url);
		curl_setopt($ch,  CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array('accept: application/json',));
		$response = curl_exec($ch);
		return $response;
	}
	
	private function getToken(){
		$ch = curl_init($this->host . "/identity/oauth/token?grant_type=client_credentials&client_id=" . $this->clientId . "&client_secret=" . $this->clientSecret);
		curl_setopt($ch,  CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array('accept: application/json'));
		$response = json_decode(curl_exec($ch));
		curl_close($ch);
		$token = $response->access_token;
		return $token;
	}
}