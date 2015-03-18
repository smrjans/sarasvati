# Rubric #

Sarasvati includes a domain specific language (DSL) for defining guards. A Rubric program consists of a single statement, which has the following form:

```
 STMT = 'if' EXPR 'then' STMT 'else' STMT
      | Accept 
      | Discard 
      | Skip 
      | Skip ARCNAME
      | Delay until DATE
      | digit
      | "quoted string"
      | DATE

 DATE= = '(' DATEFUNCTION ')'
       | '(' NUMBER UNIT (before|after) DATEFUNCTION ')'

 UNIT = second
      | seconds
      | minute
      | minutes
      | hour
      | hours
      | day
      | days
      | week
      | weeks

 EXPR = PREDICATE
      | PREDICATE 'or' EXPR
      | PREDICATE 'and' EXPR
      | 'not' EXPR
      | '(' EXPR ')'

 PREDICATE = ID
 DATEFUNCTION = ID
 ID = (letter)(letter|digit|'.')*  // letter followed by 0 to many letters, digits or dots

```

A `predicate` is a string identifier, which the runtime ties to a function which returns a boolean value. The function may have access to the current process environment.

The statement result can be any of the following:

  * A `GuardResponse`
  * A String
  * A number
  * A date function
  * An offset to a date function

Guards should, of course, always return instances of `GuardResponse`. The advantage of using this DSL for defining node guards, is that it can help users understand process behaviour. Given a library of predicates that are named with user comprehension in mind, Rubric statements should be understandable by users with a minimum of technical understanding and training.

For example, a process for order fulfillment may include a set of steps for orders with regular shipping and a different set of steps for priority shipping. Priority shipping will be given to customers who have requested for a specific order, or for certain high volume customers. A set of predicates could be defined.

  * `Order.isPriority`
  * `Customer.isHighVolume`

The node making the decision of which steps to take, might be defined as follows:

```
  <node name="isPriority">
   <guard>
     if (Order.isPriority or Customer.isHighVolume)
       then SKIP priority
       else SKIP regular
    </guard>

    <arc name="regular" to="regularShipping"/>
    <arc name="priority" to="priorityShipping"/>
  </node>
```

This Rubric statement could be presented directly to the users. Or, if the SKIP naming was deemed to confusing, it could be parsed and restated as:

```
  if order is priority or customer is high volume
     then do priority
     else do regular
```

This, or course, relies on appropriate naming of predicates and arcs. Assuming that predicates are side-effect free (which they should be, of course), then they can also be evaluated at any time. This would allow the system to display the current values for each of the predicates used in a guard.

## Rubric API ##
There are two parts to the API.
  * Defining predicates
  * Evaluating statements

To define a predicate, implement the `RubricPredicate` interface. For example, here is a predicate which returns true if is a weekend:

```
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;

public class IsWeekendPredicate implements RubricPredicate
{
  @Override
  public boolean evaluate (Engine engine, NodeToken token)
  {
    Calendar cal = Calendar.getInstance();
    int day = cal.get( Calendar.DAY_OF_WEEK );
    return day == Calendar.SATURDAY ||
           day == Calendar.SUNDAY;
  }
}
```

This predicate can be registered with the `RubricFunctionRepository`, which be used to construct a `RubricEnv`.

The `RubricInterpreter` will take Rubric statements and compile them to a `RubricStmt`. Passing a `RubricEnv` to the `RubricStmt.eval` method will turn evaluation results.

If using Rubric for guards, the Engine will handle most of these details for you. The default implementation uses `DefaultRubricEnv` and the global instance of `DefaultRubricFunctionRepository`. However,this can be overridden by subclassing the appropriate instance of Engine and overriding the `newRubricEnv` method.

```
package com.googlecode.sarasvati.rubric;

import java.util.Calendar;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.rubric.env.DefaultRubricEnv;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;

public class IsWeekendPredicate implements RubricPredicate
{
  @Override
  public boolean eval (Engine engine, NodeToken token)
  {
    Calendar cal = Calendar.getInstance();
    int day = cal.get( Calendar.DAY_OF_WEEK );
    return day == Calendar.SATURDAY ||
           day == Calendar.SUNDAY;
  }

  public static void main (String[] args) throws Exception
  {
    DefaultRubricFunctionRepository repository = DefaultRubricFunctionRepository.getGlobalInstance();
    repository.registerPredicate( "isWeekend", new IsWeekendPredicate() );

    String stmt = "if isWeekend then Accept else Skip";

    RubricEnv env = new DefaultRubricEnv( null, null, repository );
    GuardResponse response = (GuardResponse)RubricInterpreter.compile( stmt ).eval( env );

    System.out.println( response );
  }
}

```

## Date Function ##
Date functions are similar to predicates. They are functions which return dates instead of booleans. They can implement the `RubricDateFunction` interface and be stored in a `RubricFunctionRepository`.

The `RubricEnv` interface also defines a method

```
  Date evalRelativeDate (Date date, int offset, int unit);
```

The `DefaultRubricEnv` uses Calendar to implement this, however subclasses can override this to provide offset rules specific to business requirements.

## Other Uses ##
Rubric can be used as (very) simple rules engine. For example, it could be used to specify the attributes of a task in a process definition.