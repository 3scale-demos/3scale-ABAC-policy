local _M = require('apicast.policy').new('jwt_ABAC_Authorizer', '1.0.0')
local new = _M.new

function _M.new(configuration)
  local self = new()
  local ops = {}
  local config = configuration or {}
  self.ops = ops
  self.author_rest_endpoint=config.author_rest_endpoint
  self.JWT_claim_name=config.JWT_claim_name
  self.error_message=config.error_message
   return self
end

local function isempty(s)
  return s == nil or s == ''
end

local function check_authorization(auth_endpoint,role,method,resource)
      local is_authorized=false
      if isempty(auth_endpoint) or isempty(role) or isempty(method) or isempty(resource) then
       return is_authorized
      end
      local ops = {}
      local query={}
      query.role=role
      query.method=method
      query.resource=resource
      local httpc = require("resty.http").new()
      local res, err = httpc:request_uri(auth_endpoint, {
        method = "GET",
        body = "",
        query=query,
        headers = {
            ["Content-Type"] = "application/json",
        },
      })
if not res then
    ngx.log(ngx.ERR, "authorization service request failed: ", err)
    return is_authorized 
end
  if res then
      ngx.log(ngx.ERR, "authprization service request success: ", res.body)
      if not isempty(res.body) and string.find(res.body, "true") then
          return true
      end
  end      
      return is_authorized
end

local function deny_request(error_msg)
  ngx.status = ngx.HTTP_FORBIDDEN
  ngx.say(error_msg)
  ngx.exit(ngx.status)
end


function _M:access(context)
  local uri = ngx.var.uri
  local request_method =  ngx.req.get_method()
  local is_auth=check_authorization( self.author_rest_endpoint,context.jwt[self.JWT_claim_name],request_method,uri)
  if  is_auth == false then
   return deny_request(self.error_message)
  end   
end  
return _M
