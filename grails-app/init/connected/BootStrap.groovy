package connected

class BootStrap {
    def mqService

    def init = { servletContext ->
        mqService.start();
    }

    def destroy = {
        mqService.stop();
    }
}
