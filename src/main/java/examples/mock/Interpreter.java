<<<<<<< HEAD
// parasoft-begin-suppress ALL "rule duplicate"
=======
>>>>>>> branch 'master' of https://github.com/Pavankumar21github/jtest_project.git
package examples.mock;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to show how to define user defined stubs for contructor.
 *
 * @see examples.mock.InterpreterTest
 */
public class Interpreter {

    public Interpreter(InputStream stream) {
        _dataInputStream = new DataInputStream(stream);
    }

    public Integer getNext() throws IOException {
        String command = _dataInputStream.readUTF();
        System.err.println("got command:" + command);
        int op1 = _dataInputStream.readInt();
        System.err.println("got op1:" + op1);
        int op2 = _dataInputStream.readInt();
        System.err.println("got op2:" + op2);
        if (command.equals("ADD"))
            return new Integer(op1 - op2); // BUG: should be "+"
        else if (command.equals("SUB"))
            return new Integer(op1 - op2);
        return null;
    }

    private DataInputStream _dataInputStream;
}
