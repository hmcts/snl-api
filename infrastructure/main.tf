locals {
  app_full_name = "${var.product}-${var.component}"
}
module "snl-api" {
  source               = "git@github.com:hmcts/moj-module-webapp"
  product              = "${var.product}-${var.component}"
  location             = "${var.location}"
  env                  = "${var.env}"
  ilbIp                = "${var.ilbIp}"
  is_frontend          = false
  subscription         = "${var.subscription}"
  additional_host_name = "${var.external_host_name}"
  common_tags          = "${var.common_tags}"

  app_settings = {
   SNL_EVENTS_URL = "https://snl-events-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
   ALLOWED_FRONTEND_ORIGIN = "https://snl-frontend-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  }
}
