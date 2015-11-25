package core.models;


public class Metric {

    private int id;

    private String title;

    private String command;

    public Metric() {
    }

    public Metric(int id ,String title, String command) {
        this.id=id;
        this.title = title;
        this.command = command;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

//    public double getMetricValue(InputStream in) {
//        try {
//            byte[] tmp = new byte[1024];
//            double result = 0;
//            while (true) {
//                while (in.available() > 0) {
//                    int i = in.read(tmp, 0, 1024);
//                    if (i < 0) return 0;
//                    return summRows(Arrays.copyOfRange(tmp, 0, i));
//                }
//            }
//        } catch (Exception ee) {
//            System.out.println("�������� ������ = null");
//        }
//        return 0;
//    }
//
//    private double summRows(byte[] array) throws UnsupportedEncodingException {
//        List<Byte> tmp = new ArrayList<Byte>();
//        StringBuilder resulttemp= new StringBuilder();
//        double result = 0;
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] != 0) {
//                if (array[i] == 10) {
//                    for (byte item : tmp) {
//                        resulttemp.append((char) item);
//                    }
//                    result += Double.parseDouble(resulttemp.toString());
//                    resulttemp.setLength(0);
//                    tmp.clear();
//
//                } else {
//                    tmp.add(array[i]);
//                }
//            }
//            else return result;
//        }
//        return result;
//    }
}
