locals {
  app_full_name = "${var.product}-${var.component}"

  aat_events_url = "http://snl-events-aat.service.core-compute-aat.internal"
  local_events_url = "http://snl-events-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  events_url = "${var.env == "preview" ? local.aat_events_url : local.local_events_url}"
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
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags          = "${var.common_tags}"

  app_settings = {
   SNL_EVENTS_URL = "http://pr-129-snl-events-preview.service.core-compute-preview.internal"
  }
}
