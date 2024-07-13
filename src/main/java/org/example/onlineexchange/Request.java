package org.example.onlineexchange;

public class Request{

    private final String command;
    private final String[] parameters;

    private Request(String msg) {
        command = msg.substring(msg.indexOf('[') + 1, msg.indexOf(']'));
        msg = msg.substring(msg.indexOf(',') + 1);
        parameters = msg.split(",");
    }

    public Request(String command, String... parameters){
        this.parameters = parameters;
        this.command = command;
    }

    public static Request requestProcessor(String msg){
        return new Request(msg);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(STR."[\{command}]");

        for (String par : parameters){
            result.append(STR.",\{par}");
        }
        result.append("\n");
        return result.toString();
    }

    public String getCommand() {
        return command;
    }

    public String getParameter(int i) {
        return parameters[i];
    }

    public int getParameterSize(){
        return parameters.length;
    }
}
